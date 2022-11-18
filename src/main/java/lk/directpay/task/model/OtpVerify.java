package lk.directpay.task.model;


import lombok.*;
import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OtpVerify {

    @NotEmpty(message = "email must not be a null")
    @Pattern(regexp = "^(.+)@(.+)$",message = "Invalid Email...")
    private String email;

    @NotEmpty(message = "email must not be a null")
    @Pattern(regexp = "^(.+)@(.+)$",message = "Invalid Email...")
    private String mobile;

    @NotEmpty(message = "Otp must not be a null")
    private String otp;


}
