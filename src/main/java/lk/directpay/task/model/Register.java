package lk.directpay.task.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Register {

        private String firstname;
        private String lastname;
        private String nic;
        private String email;
        private String mobile;
        private String password;
        private String countryId;
        private String status;
        private String username;
        private Device device;

    }


