package lk.directpay.task.controller;


import lk.directpay.task.entity.AppUser;
import lk.directpay.task.model.DefaultResponse;
import lk.directpay.task.model.NicVerificationRequest;
import lk.directpay.task.model.OtpVerify;
import lk.directpay.task.model.ValidateRegister;
import lk.directpay.task.repository.UserRepository;
import lk.directpay.task.services.DeviceRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
/*import org.springframework.util.StringUtils;*/
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

@RestController
@RequestMapping(path = "api/user")
public class userController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    DeviceRegistrationService deviceRegistrationService;

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private final String NAME_PATTERN_REGEX = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$";
    private final String NIC_PATTERN_REGEX = "^([0-9]{9}[x|X|v|V]|[0-9]{12})$";

    public @PostMapping(path = "/register/otp")
    DefaultResponse registerOtp(@RequestBody OtpVerify otpVerify) {
        return deviceRegistrationService.registerDevice(otpVerify);
    }

    @PostMapping(path = "/nic/verify")
    public DefaultResponse verifyUserNic(@RequestBody NicVerificationRequest nicVerificationRequest) {
        String username = nicVerificationRequest.getUserId();
        String mobile = nicVerificationRequest.getMobile();
        String nic = nicVerificationRequest.getNic();

        if (StringUtils.isEmpty(username) && StringUtils.isEmpty(mobile)){
            return DefaultResponse.error("Failed", "Cannot find user without user id or mobile");
        }
        if (StringUtils.isEmpty(nic)){
            return DefaultResponse.error("Failed", "NIC is empty");
        }
        if (!Pattern.matches(NIC_PATTERN_REGEX, nic)){
            return DefaultResponse.error("Failed", "Invalid NIC format");
        }
        AppUser appUser = null;
        if (!StringUtils.isEmpty(username)){
            appUser = userRepository.findOneByUsername(username);
        }else if (!StringUtils.isEmpty(mobile)){
            appUser = userRepository.findAppUserByPhoneNumber(mobile);
        }else {
            return DefaultResponse.error("Failed", "empty parameters");
        }
        if (appUser != null){
            if (nic.equalsIgnoreCase(appUser.getNic())){
                return DefaultResponse.success("Success", "NIC verification successful");
            }else {
                return DefaultResponse.error("Failed", "NIC doesn't match");
            }
        }else {
            return DefaultResponse.error("Failed", "Cannot find user");
        }
    }

    public @PostMapping(path = "/register/validate")
    DefaultResponse validate(@RequestBody ValidateRegister validateRegister) {
        HashMap<String, Object> validations = new HashMap();

        final String firstname =validateRegister.getFirstname();
        final String lastname =validateRegister.getLastname();
        final String nic =validateRegister.getNic();

        if (!Pattern.matches(NAME_PATTERN_REGEX, firstname)) {
            validations.put("firstname", "Invalid characters in First Name!");
        }

        if (!Pattern.matches(NAME_PATTERN_REGEX, lastname)) {
            validations.put("lastname", "Invalid characters in Last Name!");
        }

        if (!Pattern.matches(NIC_PATTERN_REGEX, nic)) {
            validations.put("nic", "Invalid NIC format!");
        }

        boolean isValidatedNIC = validateNIC(nic);
        if (!isValidatedNIC){
            LOGGER.log(Level.SEVERE, "User with this NIC already exists!");
            validations.put("nic", "User with this NIC already exists!");
        }


        if (!validations.isEmpty()) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("validations", validations);
            return new DefaultResponse(400, "Failed", "Validation errors", data);
        } else {
            return new DefaultResponse(200, "Success", "No validation errors found!");
        }
    }


    private boolean validateNIC(String nic){
        if (nic.length() == 12){
            String oldNic = getFormattedNIC(nic);
            // Check if old NIC exists
            AppUser userByNic = userRepository.findByOldNic(oldNic);
            if (Objects.nonNull(userByNic)){
                LOGGER.log(Level.SEVERE, "User with this NIC already exists!");
                return false;
            } else {
                //Check if new NIC exists
                userByNic = userRepository.findByNic(nic);
                if (Objects.nonNull(userByNic)){
                    LOGGER.log(Level.SEVERE, "User with this NIC already exists!");
                    return false;
                }
            }
            return true;
        }

        if (nic.length() == 10){
            String newNicWith0 = getFormattedNIC(nic);
            int index = 7;
            String character = "1";
            String newNicWith1 = newNicWith0.substring(0, index) + character + newNicWith0.substring(index+1); // result -> 1993339[0]0268 --> 1993339[1]0268

            AppUser userByNic = userRepository.findByOldNic(nic);
            if (Objects.nonNull(userByNic)){
                LOGGER.log(Level.SEVERE, "User with this NIC already exists!");
                return false;
            } else {
                List<AppUser> usersByNic = userRepository.findByNewNic(newNicWith0, newNicWith1);
                if (!usersByNic.isEmpty()){
                    LOGGER.log(Level.SEVERE, "User with this NICs already exists!");
                    return false;
                }
            }
            return true;
        }
        return false;
    }


    private String getFormattedNIC(String nic) {
        if (nic.length() == 12) {
            StringBuilder sb = new StringBuilder(nic.substring(2));
            nic = sb.deleteCharAt(5).toString();
            LOGGER.log(Level.INFO, "getFormattedNIC-> " + nic);
        } else if (nic.length() == 10){
            //Old NIC to New NIC, nic --- > 933390268V
            StringBuilder sb = new StringBuilder(nic.substring(0, nic.length() - 1)); // result -> 933390268
            sb = sb.insert(0, "19"); // result -> 19933390268
            nic = sb.insert(7, "0").toString(); // result -> 1993339[0]0268
            LOGGER.log(Level.INFO, "getFormattedNIC -> " + nic);
        }
        return nic;
    }

}
