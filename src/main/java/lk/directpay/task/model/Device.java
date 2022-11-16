package lk.directpay.task.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class Device {

        private String platform;
        @JsonProperty("device_id")
        private String deviceId;
        @JsonProperty("app_version")
        private String appVersion;

        public Device(String platform, String deviceId, String appVersion) {
            this.platform = platform;
            this.deviceId = deviceId;
            this.appVersion = appVersion;
        }

        public Device(String platform, String appVersion) {
            this.platform = platform;
            this.appVersion = appVersion;
        }

    }


