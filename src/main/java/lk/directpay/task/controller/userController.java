package lk.directpay.task.controller;


import lk.directpay.task.entity.*;
import lk.directpay.task.model.*;
import lk.directpay.task.repository.*;
import lk.directpay.task.services.AuthUserDetailsService;
import lk.directpay.task.services.DeviceRegistrationService;
import lk.directpay.task.utility.AppConstants;
import lk.directpay.task.utility.JWTUtility;
import lk.directpay.task.utility.Translator;
import lk.directpay.task.utility.ValidationUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

@RestController
@RequestMapping(path = "api/user")
public class userController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ValidationUtility validations;

    @Autowired
    OtpVerificationRepository otpVerificationRepository;

    @Autowired
    CountryRepository countryRepository;

    @Autowired
    AuthUserDetailsService userDetailsService;

    @Autowired
    JWTUtility jwtUtility;

    @Autowired
    ParameterRepository parameterRepository;

    @Autowired
    BillerFeeRepository billerFeeRepository;

    @Autowired
    DeviceRegistrationService deviceRegistrationService;

    @Value("${transaction.limit}")
    private String transactionLimit;

    @Value("${auth.transaction.limit}")
    private String authTransactionLimit;


    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    /*private final String NAME_PATTERN_REGEX = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$";
    private final String NIC_PATTERN_REGEX = "^([0-9]{9}[x|X|v|V]|[0-9]{12})$";*/

    public @PostMapping(path = "/register/otp")
    DefaultResponse registerOtp(@RequestBody @Valid OtpRegister otpRegister) {
        return deviceRegistrationService.registerDevice(otpRegister);
    }

    @PostMapping(path = "/nic/verify")
    public DefaultResponse verifyUserNic(@RequestBody @Valid NicVerificationRequest nicVerificationRequest) {
        String username = nicVerificationRequest.getUserId();
        String mobile = nicVerificationRequest.getMobile();
        String nic = nicVerificationRequest.getNic();

       /* if (StringUtils.isEmpty(username) && StringUtils.isEmpty(mobile)){
            return DefaultResponse.error("Failed", "Cannot find user without user id or mobile");
        }
        if (StringUtils.isEmpty(nic)){
            return DefaultResponse.error("Failed", "NIC is empty");
        }
        if (!Pattern.matches(NIC_PATTERN_REGEX, nic)){
            return DefaultResponse.error("Failed", "Invalid NIC format");
        }*/
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
    DefaultResponse validate(@RequestBody @Valid ValidateRegister validateRegister) {
        HashMap<String, Object> validations = new HashMap();

        final String firstname =validateRegister.getFirstname();
        final String lastname =validateRegister.getLastname();
        final String nic =validateRegister.getNic();

        /*if (!Pattern.matches(NAME_PATTERN_REGEX, firstname)) {
            validations.put("firstname", "Invalid characters in First Name!");
        }

        if (!Pattern.matches(NAME_PATTERN_REGEX, lastname)) {
            validations.put("lastname", "Invalid characters in Last Name!");
        }

        if (!Pattern.matches(NIC_PATTERN_REGEX, nic)) {
            validations.put("nic", "Invalid NIC format!");
        }*/

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

    public @PostMapping(path = "/register/verify")
    DefaultResponse verify( @RequestBody @Valid OtpVerify otpVerify) {
        return deviceRegistrationService.verifyDevice(otpVerify);
    }

    public @PostMapping(path = "/register")
    DefaultResponse register(@RequestBody @Valid Register register) {

        HashMap<String, Object> deviceDetails = new HashMap<>();
        deviceDetails.put("platform",register.getDevice().getPlatform());
        deviceDetails.put("app_version",register.getDevice().getAppVersion());
        deviceDetails.put("device_id",register.getDevice().getDeviceId());


        HashMap<String, Object> payload = new HashMap<>();
        payload.put("firstname",register.getFirstname());
        payload.put("lastname",register.getLastname());
        payload.put("nic",register.getNic());
        payload.put("email",register.getEmail());
        payload.put("mobile",register.getMobile());
        payload.put("password",register.getPassword());
        payload.put("countryId",register.getCountryId());
        payload.put("status",register.getStatus());
        payload.put("username",register.getUsername());
        payload.put("device",deviceDetails);


        try {

            /*// validations
            String validationMessage = validations.required(payload, "password", "firstname", "lastname", "mobile",
                    "email", "nic"*//*, "nic_front", "nic_back"*//*);

            if (validationMessage != null) {
                LOGGER.log(Level.SEVERE, "Validation Errors : " + validationMessage);
                return new DefaultResponse(400, "Failed", validationMessage);
            }*/

            // credentials
            final String username = UUID.randomUUID().toString();
            String password =
          /*  crypto.decrypt(*/
            payload.get("password").toString();

            /*if (password == null) {
                LOGGER.log(Level.SEVERE, "login failed. decrypt error");
                return new DefaultResponse(400, "Failed", "");
            }*/

            // personal info
            final String firstname = payload.get("firstname").toString().trim();
            final String lastname = payload.get("lastname").toString().trim();
            final int country = payload.containsKey("country") ? (int) payload.get("country") : 1;
            final String mobile = payload.get("mobile").toString().trim();
            final String email = payload.get("email").toString().trim();
            final String nic = payload.get("nic").toString().toUpperCase().trim();
            /*final String nicFront = payload.get("nic_front").toString();
            final String nicBack = payload.get("nic_back").toString();*/

            // meta data
            HashMap<String, Object> device = (HashMap<String, Object>) payload.get("device");
            final String deviceId = device.get("device_id").toString();
            final String appVersion = device.get("app_version").toString();

            final List<AppUser> usersByEmail = userRepository.findByEmail(email);
            if (usersByEmail.size() > 0) {
                LOGGER.log(Level.WARNING, "User with this email already exists!");
                return new DefaultResponse(400, Translator.toLocale("failed"), Translator.toLocale("user_exists_with_email"));
            }

            final List<AppUser> usersByMobile = userRepository.findByPhoneNumber(mobile);
            if (usersByMobile.size() > 0) {
                LOGGER.log(Level.WARNING, "User with this mobile already exists!");
                return new DefaultResponse(400, Translator.toLocale("failed"), Translator.toLocale("user_exists_with_mobile"));
            }

            // Validate NIC
            boolean isValidatedNIC = validateNIC(nic);
            if (!isValidatedNIC){
                LOGGER.log(Level.SEVERE, "User with this NIC already exists!");
                return new DefaultResponse(400, Translator.toLocale("failed"), Translator.toLocale("user_exists_with_nic"));
            }

            final Optional<Country> countryOptional = countryRepository.findById(country);
            if (countryOptional.isEmpty()) {
                LOGGER.log(Level.WARNING, "Invalid country selected!");
                return new DefaultResponse(400, Translator.toLocale("failed"), Translator.toLocale("invalid_country"));
            }

            AppUser appUser = new AppUser();

            appUser.setFirstname(firstname);
            appUser.setLastname(lastname);
            appUser.setUsername(username);
            appUser.setEmail(email);
            appUser.setPassword(password);
            appUser.setCountryId(country);
            appUser.setPhoneNumber(mobile);
            appUser.setNic(nic.toUpperCase());
            /*appUser.setNicFront(nicFront);
            appUser.setNicBack(nicBack);*/
            appUser.setStatus(1);


            if (payload.containsKey("fcm_token") && !payload.get("fcm_token").equals("")) {
                appUser.setFcmToken(payload.get("fcm_token").toString());
            }

            // save metadata
            appUser.setDeviceId(deviceId);
            appUser.setAppVersion(appVersion);

            ArrayList<String> roles = new ArrayList<>();
            roles.add("user");
            appUser.setRoles(roles);

            List<OtpVerification> otpVerificationList = otpVerificationRepository
                    .findOtpVerificationByMobile(appUser.getPhoneNumber());
            if (!otpVerificationList.isEmpty()) {
                final OtpVerification otpVerification = otpVerificationList.get(0);
                if (otpVerification.getMobileVerified().equals(VerificationStatus.VERIFIED.name())) {
                    userRepository.save(appUser);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(appUser.getUsername());
                    String token = jwtUtility.generateToken(userDetails);
                    Map<String, Object> data = new HashMap<String, Object>();
                    data.put("user", createUserResponseObject(appUser));
                    data.put("token", token);

                    //fees
                    data.put("fund_transfer", getFundTransferFees());
                    data.put("credit_card_payment", getCreditCardPaymentFees());
                    data.put("biller_fees", this.getBillerFees());

                    Map<String, Object> msgObject = new HashMap<>();
                    msgObject.put("number", appUser.getPhoneNumber());
                    msgObject.put("message",
                            "Hi " + appUser.getFirstname() + ", You have Successfully Signed up for NSB Pay.");
                    /*commonSmsService.sendMessage(msgObject);

                    final boolean isForeign = !appUser.getPhoneNumber().startsWith("94");
                    if (isForeign) {
                        String subject = "NSBPAY - WELCOME....!";
                        sendEmail(appUser.getEmail(), subject, "Hi " + appUser.getFirstname() + ", You have Successfully Signed up for NSB Pay.");
                    }*/

                    return new DefaultResponse(200, Translator.toLocale("success"), Translator.toLocale("user_registered"), data);
                } else {
                    return new DefaultResponse(400, Translator.toLocale("failed"), Translator.toLocale("device_verification_failed"),
                            payload);
                }
            } else {
                return new DefaultResponse(400, Translator.toLocale("failed"),
                        Translator.toLocale("user_not_verified"), payload);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DefaultResponse().setMessage("Please try again. If the problem persists please contact customer support");
    }

    public UserResponse createUserResponseObject(AppUser user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(user.getUsername());
        userResponse.setFirstname(user.getFirstname());
        userResponse.setLastname(user.getLastname());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        userResponse.setEmail(user.getEmail());
        userResponse.setNic(user.getNic());
        userResponse.setDeviceId(user.getDeviceId());
        userResponse.setTransactionAuthLimit(authTransactionLimit);
        userResponse.setMaxTransactionLimit(transactionLimit);
        userResponse.setDefaultTill(user.getDefaultTillId());
        return userResponse;
    }

    private Map<String, Object> getFundTransferFees() {

        Map<String, Object> fees = new HashMap<>();

        Parameter commissionValue1 = parameterRepository.findParameterByName(AppConstants.NSB_TO_NSB_COMMISSION);
        double commission1 = Double.parseDouble(commissionValue1.getValue());
        fees.put(AppConstants.NSB_TO_NSB_COMMISSION.toLowerCase(), commission1);

        Parameter commissionValue2 = parameterRepository.findParameterByName(AppConstants.NSB_TO_OTHER_COMMISSION);
        double commission2 = Double.parseDouble(commissionValue2.getValue());
        fees.put(AppConstants.NSB_TO_OTHER_COMMISSION.toLowerCase(), commission2);

        Parameter commissionValue3 = parameterRepository.findParameterByName(AppConstants.OTHER_TO_NSB_COMMISSION);
        double commission3 = Double.parseDouble(commissionValue3.getValue());
        fees.put(AppConstants.OTHER_TO_NSB_COMMISSION.toLowerCase(), commission3);

        Parameter commissionValue4 = parameterRepository.findParameterByName(AppConstants.OTHER_TO_OTHER_COMMISSION);
        double commission4 = Double.parseDouble(commissionValue4.getValue());
        fees.put(AppConstants.OTHER_TO_OTHER_COMMISSION.toLowerCase(), commission4);

        fees.put("bank_code", AppConstants.NSB_BANK_CODE);

        return fees;
    }

    private Map<String, Object> getCreditCardPaymentFees() {

        Map<String, Object> fees = new HashMap<>();

        Parameter commissionValue1 = parameterRepository.findParameterByName(AppConstants.CREDIT_CARD_PAYMENT_FROM_NSB_COMMISSION);
        double commission1 = Double.parseDouble(commissionValue1.getValue());
        fees.put(AppConstants.CREDIT_CARD_PAYMENT_FROM_NSB_COMMISSION.toLowerCase(), commission1);

        Parameter commissionValue2 = parameterRepository.findParameterByName(AppConstants.CREDIT_CARD_PAYMENT_FROM_OTHER_COMMISSION);
        double commission2 = Double.parseDouble(commissionValue2.getValue());
        fees.put(AppConstants.CREDIT_CARD_PAYMENT_FROM_OTHER_COMMISSION.toLowerCase(), commission2);

        fees.put("bank_code", AppConstants.NSB_BANK_CODE);

        return fees;
    }

    private HashMap<String, Object> getBillerFees() {
        List<BillerFee> billerFeeList = billerFeeRepository.findAll();
        HashMap<String, Object> billerFees = new HashMap<>();
        for (BillerFee billerFee : billerFeeList) {
            billerFees.put(billerFee.getBillerId(), billerFee.getFee());
        }
        return billerFees;
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
