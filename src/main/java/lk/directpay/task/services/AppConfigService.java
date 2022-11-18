package lk.directpay.task.services;

import lk.directpay.task.entity.AppConfig;
import lk.directpay.task.entity.Country;
import lk.directpay.task.model.AppConfigResponse;
import lk.directpay.task.model.DefaultResponse;
import lk.directpay.task.model.Device;
import lk.directpay.task.repository.AppConfigRepository;
import lk.directpay.task.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


@Service
public class AppConfigService {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private final AppConfigRepository appConfigRepository;
    private final CountryRepository countryRepository;


    @Autowired
    public AppConfigService(AppConfigRepository appConfigRepository , CountryRepository countryRepository) {
        this.appConfigRepository = appConfigRepository;
        this.countryRepository = countryRepository;
     }

    public DefaultResponse checkForDeviceUpdates(Device deviceData) {
        /*HashMap<String, Object> deviceData = (HashMap<String, Object>) payload.get("device");*/
        String platform = deviceData.getPlatform();
        AppConfig appConfig = appConfigRepository.findAppConfigByPlatformIgnoreCase(platform);
        if (appConfig != null) {
            double deviceVersion = Double.parseDouble(deviceData.getAppVersion());
            double minAppVersion = Double.parseDouble(appConfig.getMinVersion());
            double latestAppVersion = Double.parseDouble(appConfig.getLatestVersion());
            Map<String, Object> data;
            if (deviceVersion >= minAppVersion) {
                if (deviceVersion < latestAppVersion) {
                    LOGGER.log(Level.INFO, "There is an update to install...app-version : " + deviceVersion + " latest version : " + latestAppVersion);
                    data = creteAppConfigResponse(appConfig, false, true, "Update your app to latest version");
                    return new DefaultResponse(200, "Update Available", "Update your app to latest version " + latestAppVersion, data);
                } else {
                    LOGGER.log(Level.INFO, "App version is up to date.");
                    data = creteAppConfigResponse(appConfig, false, false, "App is up to date");
                    return new DefaultResponse(200, "Success", "App is up to date", data);
                }
            } else {
                LOGGER.log(Level.SEVERE, "User app version is lower than the minimum version");
                data = creteAppConfigResponse(appConfig, true, true, "Update need to proceed further");
                return new DefaultResponse(200, "Update Required", "Update need to proceed further", data);
            }
        } else {
            LOGGER.log(Level.SEVERE, "Invalid Platform...");
            return new DefaultResponse(400, "Failed", "Invalid Platform");
        }
    }


    public Map<String, Object> creteAppConfigResponse(AppConfig appConfig, boolean forceUpdate, boolean updateAvailable, String message) {
        Map<String, Object> data = new HashMap<>();
        AppConfigResponse appConfigResponse = new AppConfigResponse();
        appConfigResponse.setPlatform(appConfig.getPlatform());
        appConfigResponse.setLatestVersion(appConfig.getLatestVersion());
        appConfigResponse.setAppUrl(appConfig.getAppUrl());
        appConfigResponse.setAuthScreen(appConfig.getAuthScreen());
        appConfigResponse.setSplashScreen(appConfig.getSplashScreen());

        List<Country> countries = countryRepository.findAllByStateOrderByName(CountryRepository.STATE_ACTIVE);
        ArrayList<HashMap<String, Object>> countryList = new ArrayList<>();
        countries.forEach(country -> {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("id", country.getId());
                    map.put("name", country.getName());
                    map.put("code", country.getCode());
                    map.put("min_length", country.getMinLength());
                    map.put("max_length", country.getMaxLength());
                    countryList.add(map);
                }
        );
        appConfigResponse.setCountries(countryList);

        data.put("force_update", forceUpdate);
        data.put("update_available", updateAvailable);
        data.put("message", message);
        data.put("app_config_data", appConfigResponse);
        return data;
    }



}
