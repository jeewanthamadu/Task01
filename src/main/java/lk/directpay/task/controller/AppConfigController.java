package lk.directpay.task.controller;


import lk.directpay.task.model.DefaultResponse;
import lk.directpay.task.model.Device;
import lk.directpay.task.services.AppConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("api/app")
public class AppConfigController {

    private final AppConfigService appConfigService;

    @Autowired
    public AppConfigController(AppConfigService appConfigService) {
        this.appConfigService = appConfigService;
    }

   @PostMapping("/config/check/update")
    public DefaultResponse checkAppUpdates(@RequestBody Device Device) {
       return appConfigService.checkForDeviceUpdates(Device);
    }


}
