package lk.directpay.task.controller;


import lk.directpay.task.model.DefaultResponse;
import lk.directpay.task.services.DeviceRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping(path = "api/user")
public class userController {

    @Autowired
    DeviceRegistrationService deviceRegistrationService;

    public @PostMapping(path = "/register/otp")
    DefaultResponse registerOtp(
            @RequestBody HashMap<String, Object> payload) {
        return deviceRegistrationService.registerDevice(payload);
    }


}
