package lk.directpay.task.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Device {

        @NotNull(message = "Platform must not be a null")
        private String platform;

        @JsonProperty("device_id")
        @NotEmpty(message = "DeviceId must not be a null")
        private String deviceId;

        @JsonProperty("app_version")
        @NotEmpty(message = "AppVersion must not be a null")
        private String appVersion;


    }


