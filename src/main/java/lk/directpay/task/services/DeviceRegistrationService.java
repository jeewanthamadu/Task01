package lk.directpay.task.services;


import lk.directpay.task.entity.OtpVerification;
import lk.directpay.task.entity.VerificationStatus;
import lk.directpay.task.model.DefaultResponse;
import lk.directpay.task.model.OtpRegister;

import lk.directpay.task.model.OtpVerify;
import lk.directpay.task.repository.OtpVerificationRepository;
import lk.directpay.task.repository.UserRepository;
import lk.directpay.task.utility.Translator;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class DeviceRegistrationService {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static final List<Integer> digits = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
    private final OtpVerificationRepository otpVerificationRepository;
    private final UserRepository userRepository;


    @Autowired
    public DeviceRegistrationService(OtpVerificationRepository otpVerificationRepository, UserRepository userRepository) {
        this.otpVerificationRepository = otpVerificationRepository;
        this.userRepository = userRepository;

    }

    public DefaultResponse registerDevice( OtpRegister otpRegister ) {
        String email = otpRegister.getEmail();
        String mobile = otpRegister.getMobile();
        String phn = mobile.substring(0, 4);
        HashMap<String,Object> payload = new HashMap<>();
        payload.put("email",email);
        payload.put("mobile",mobile);
        if (phn.charAt(0) == '+') {
            phn = phn.substring(1, phn.length() - 1);
        }
        final boolean isForeign = !phn.startsWith("94");
        String otp = createOTP();
        int attempts = 0;
        OtpVerification otpVerification;
        if (!userRepository.findByEmail(email).isEmpty()) {
            LOGGER.log(Level.WARNING, "User already registered for email");
            return new DefaultResponse(400, Translator.toLocale("failed"),
                    Translator.toLocale("user_registered_for_email"), payload);
        }
        if (userRepository.findByPhoneNumber(mobile).isEmpty()) {
            List<OtpVerification> otpVerificationList = otpVerificationRepository.findOtpVerificationByMobile(mobile);
            if (!otpVerificationList.isEmpty()) {
                otpVerification = otpVerificationList.get(0);
            } else {
                ResponseEntity responseEntity = sendOTP(mobile, otp);
                /*if (isForeign) {
                    sendOTPEmail(email, otp);
                }*/
                if (responseEntity.getStatusCodeValue() == 200) {
                    return saveOTP(email, mobile, otp, attempts + 1);
                } else {
                    LOGGER.log(Level.SEVERE, responseEntity.getBody().toString());
                    return new DefaultResponse(400, Translator.toLocale("failed"),
                            Translator.toLocale("error_occurred"), payload);
                }
            }

           if (otpVerification.getAttempts() < 50) {
                ResponseEntity responseEntity = sendOTP(mobile, otp);
                if (isForeign) {
                    /*sendOTPEmail(email, otp);*/
                }
                if (responseEntity.getStatusCodeValue() == 200) {
                    if (!otpVerification.getEmail().equals(email)) {
                        otpVerification.setEmail(email);
                        LOGGER.info("User has changed email...");
                    }
                    return updateOTP(otpVerification, otp);
                } else {
                    LOGGER.log(Level.SEVERE, responseEntity.getBody().toString());
                    return new DefaultResponse(400, "Failed", "Error Occurred....!", payload);
                }
            } else {
                LOGGER.log(Level.SEVERE, "User otp attempts are over....!");
                return new DefaultResponse(400, Translator.toLocale("failed"),
                        Translator.toLocale("user_attempts_are_over"), payload);
            }
        } else {
            LOGGER.log(Level.WARNING, "User already registered for phone number");
            return new DefaultResponse(400, "Failed", "User already registered", payload);
        }

    }

    public DefaultResponse verifyDevice(OtpVerify otpVerify) {
        String email = otpVerify.getEmail();
        String mobile = otpVerify.getMobile();
        String otp = otpVerify.getOtp();

        HashMap<String, Object> payload = new HashMap<>();
        payload.put("email",email);
        payload.put("mobile",mobile);
        payload.put("otp",otp);

        OtpVerification device;
        List<OtpVerification> otpVerificationList = otpVerificationRepository.findOtpVerificationByMobile(mobile);
        if (!otpVerificationList.isEmpty()) {
            device = otpVerificationList.get(0);
            if (device.getOtpAttempts() < 2) {
                if (device.getOtp().equals(otp)) {
                    device.setMobileVerified(VerificationStatus.VERIFIED.name());
                    device.setOtpAttempts(device.getOtpAttempts() + 1);
                    otpVerificationRepository.save(device);
                    Map<String, Object> data = new HashMap<>();
                    data.put("email", email);
                    data.put("mobile", mobile);
                    return new DefaultResponse(200, "Success", "Verification Successful", data);
                } else {
                    device.setOtpAttempts(device.getOtpAttempts() + 1);
                    otpVerificationRepository.save(device);
                    int remainingAttempts = 3 - device.getOtpAttempts();
                    LOGGER.log(Level.WARNING, "Entered OTP is incorrect " + remainingAttempts + " attempts left..!");
                    return new DefaultResponse(400, Translator.toLocale("verification_failed"),
                            Translator.toLocale("retype_otp") + " " + remainingAttempts + " "
                                    + Translator.toLocale("attempts_left"),
                            payload);
                }
            } else {
                device.setOtpAttempts(device.getOtpAttempts() + 1);
                device.setMobileVerified(VerificationStatus.EXCEEDED.name());
                otpVerificationRepository.save(device);
                LOGGER.log(Level.SEVERE, "User OTP attempts are over");
                return new DefaultResponse(400, Translator.toLocale("failed"), Translator.toLocale("3_attempts_over"),
                        payload);
            }
        } else {
            return new DefaultResponse(400, Translator.toLocale("failed"), Translator.toLocale("try_to_resend_otp"),
                    payload);
        }
    }

    public String createOTP() {
        Collections.shuffle(digits);
        final StringBuilder sb = new StringBuilder(4);
        for (Integer digit : digits.subList(0, 4)) {
            sb.append(digit);
        }
        return sb.toString();
    }
/*
    @Value("${app.otp.hash}")
    private String APP_HASH;*/

    private ResponseEntity sendOTP(String mobile, String otp) {
        Map<String, Object> smsPayload = new HashMap<>();
        smsPayload.put("number", mobile);
        smsPayload.put("message","Dear Customer Please use following OTP : " + otp + " to complete your request.");
        /*return commonSmsService.sendMessage(smsPayload);*/
        return new ResponseEntity("Done", HttpStatus.OK);

    }

    public String encryptOTP(String otp) {
        String encryptedOTP = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(otp.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();
            encryptedOTP = String.format("%064x", new BigInteger(1, digest));
        } catch (NoSuchAlgorithmException e) {
            LOGGER.log(Level.SEVERE, "OTP Encryption failed..." + e.getMessage());
        }
        return encryptedOTP;
    }

    private DefaultResponse saveOTP(String email, String mobile, String otp, int attempts) {
        OtpVerification otpVerification = new OtpVerification();
        otpVerification.setEmail(email);
        otpVerification.setMobile(mobile);
        otpVerification.setOtp(encryptOTP(otp));
        otpVerification.setAttempts(attempts);
        otpVerification.setOtpAttempts(0);
        otpVerification.setCreatedAt(LocalDateTime.now());
        otpVerification.setEmailVerified(VerificationStatus.NOT_VERIFIED.name());
        otpVerification.setMobileVerified(VerificationStatus.SENT.name());
        otpVerificationRepository.save(otpVerification);

        Map<String, Object> data = new HashMap<>();
        data.put("email", email);
        data.put("mobile", mobile);

        LOGGER.info("OTP Successfully sent & Device Successfully saved.....");
        return new DefaultResponse(200, "Success", "OTP Successfully sent", data);
    }

    private DefaultResponse updateOTP(OtpVerification otpVerification, String otp) {
        otpVerification.setOtp(encryptOTP(otp));
        otpVerification.setAttempts(otpVerification.getAttempts() + 1);
        otpVerification.setOtpAttempts(0);
        otpVerification.setCreatedAt(LocalDateTime.now());
        otpVerification.setEmailVerified(String.valueOf(VerificationStatus.NOT_VERIFIED));
        otpVerification.setMobileVerified(String.valueOf(VerificationStatus.SENT));
        otpVerificationRepository.save(otpVerification);
        Map<String, Object> data = new HashMap<>();
        data.put("email", otpVerification.getEmail());
        data.put("mobile", otpVerification.getMobile());
        LOGGER.log(Level.WARNING, "User has attempted " + otpVerification.getAttempts() + " times....");
        return new DefaultResponse(200, "Success", "You have attempted " + otpVerification.getAttempts() + " times",
                data);
    }


}
