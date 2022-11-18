package lk.directpay.task.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OtpRegister {

    @NotEmpty(message = "email must not be a null")
    private String email;

    @Pattern(regexp = "^[0-9]*$",message = "mobile Number Is InCorrect")
    @NotEmpty(message = "mobile Number must not be a null")
    private String mobile;


}
