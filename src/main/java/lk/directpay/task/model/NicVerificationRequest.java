package lk.directpay.task.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NicVerificationRequest {

    @JsonProperty("user_id")

    private String userId;
    @NotEmpty(message = "Dan Sapeda")
    private String nic;
    private String mobile;

}
