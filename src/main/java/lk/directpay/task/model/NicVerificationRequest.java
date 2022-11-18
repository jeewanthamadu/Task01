package lk.directpay.task.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NicVerificationRequest {

    @JsonProperty("user_id")
    @NotEmpty(message = "User id must not be a null")
    private String userId;

    @NotEmpty(message = "Nic id must not be a null")
    @Pattern(regexp = "^([0-9]{9}[x|X|v|V]|[0-9]{12})$",message = "Nic Is InCorrect")
    private String nic;

    @Pattern(regexp = "^[0-9]*$",message = "mobile Number Is InCorrect")
    @NotEmpty(message = "mobile must not be a null")
    private String mobile;

}
