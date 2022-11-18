package lk.directpay.task.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValidateRegister {

    @NotEmpty(message = "Nic id must not be a null")
    @Pattern(regexp = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$",message = "Invalid characters in First Name! ")
    private String firstname;

    @NotEmpty(message = "Nic id must not be a null")
    @Pattern(regexp = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$",message = "Invalid characters in Last Name")
    private String lastname;

    @NotEmpty(message = "Nic id must not be a null")
    @Pattern(regexp = "^([0-9]{9}[x|X|v|V]|[0-9]{12})$",message = "Invalid NIC format!")
    private String nic;


}
