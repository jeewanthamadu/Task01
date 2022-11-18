package lk.directpay.task.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckUpdateDevice {

    @NotNull(message = "Platform must not be a null")
    private String platform;


    @JsonProperty("app_version")
    @NotEmpty(message = "AppVersion must not be a null")
    private String appVersion;


}
