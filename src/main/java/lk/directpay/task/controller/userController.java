package lk.directpay.task.controller;


import lk.directpay.task.entity.AppUser;
import lk.directpay.task.model.DefaultResponse;
import lk.directpay.task.model.NicVerificationRequest;
import lk.directpay.task.model.OtpVerify;
import lk.directpay.task.repository.UserRepository;
import lk.directpay.task.services.DeviceRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
/*import org.springframework.util.StringUtils;*/
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
