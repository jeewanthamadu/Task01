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

    @Pattern(regexp = "^[0-9]*$",message = "mobile Number Is InCorrect")
    @NotEmpty(message = "mobile Number must not be a null")
    private String mobile;

    @NotEmpty(message = "Otp must not be a null")
    private String otp;


}
