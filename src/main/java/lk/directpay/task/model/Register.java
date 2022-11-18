package lk.directpay.task.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Register {
        @NotEmpty(message = "Name must not be a null")
        @Pattern(regexp = "[a-zA-Z\\s]+",message = "Invalid Name...Name should contains only letters")
        private String firstname;

        @NotEmpty(message = "Name must not be a null")
        @Pattern(regexp = "[a-zA-Z\\s]+",message = "Invalid Name...Name should contains only letters")
        private String lastname;

        @NotEmpty(message = "Nic id must not be a null")
        @Pattern(regexp = "^([0-9]{9}[x|X|v|V]|[0-9]{12})$",message = "Invalid NIC format!")
        private String nic;

        @NotEmpty(message = "email must not be a null")
        @Pattern(regexp = "^(.+)@(.+)$",message = "Invalid Email...")
        private String email;

        @Pattern(regexp = "^[0-9]*$",message = "mobile Number Is InCorrect")
        @NotEmpty(message = "mobile must not be a null")
        private String mobile;

        @NotEmpty(message = "password must not be a null")
        private String password;

        @NotEmpty(message = "countryId must not be a null")
        private String countryId;

        @NotEmpty(message = "status must not be a null")
        private String status;

        @NotEmpty(message = "username must not be a null")
        private String username;

        /*@NotEmpty(message = "device must not be a null")*/
        @Valid
        private Device device;


    }


