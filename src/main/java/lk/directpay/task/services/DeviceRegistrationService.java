package lk.directpay.task.services;


import lk.directpay.task.entity.OtpVerification;
import lk.directpay.task.model.DefaultResponse;
import lk.directpay.task.repository.UserRepository;
import lk.directpay.task.utility.Translator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class DeviceRegistrationService {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static final List<Integer> digits = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
    private final UserRepository userRepository;


    public DefaultResponse registerDevice(HashMap<String, Object> payload) {
        String email = payload.get("email").toString();
        String mobile = payload.get("mobile").toString();
        String phn = mobile.substring(0, 4);
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
                if (isForeign) {
                    sendOTPEmail(email, otp);
                }
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
                    sendOTPEmail(email, otp);
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



    public String createOTP() {
        Collections.shuffle(digits);
        final StringBuilder sb = new StringBuilder(4);
        for (Integer digit : digits.subList(0, 4)) {
            sb.append(digit);
        }
        return sb.toString();
    }






}
