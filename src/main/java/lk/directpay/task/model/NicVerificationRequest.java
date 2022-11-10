package lk.directpay.task.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NicVerificationRequest {

    @JsonProperty("user_id")
    private String userId;
    private String nic;
    private String mobile;

}
