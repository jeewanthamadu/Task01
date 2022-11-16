package lk.directpay.task.utility;

import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class Validations {
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public String validateMobile(String mobile) {
        String regex = "^[0-9]*$";
        String msg = "";
        if (!mobile.matches(regex)) {
            LOGGER.log(Level.WARNING, "Invalid Mobile...");
            msg = "Invalid Mobile";
        }
        return msg;
    }

    public String validateName(String name) {
        String regex = "[a-zA-Z\\s]+";
        String msg = "";
        if (!name.matches(regex)) {
            LOGGER.log(Level.WARNING, "Invalid Name...Name should contains only letters");
            msg = "Name should contain only letters";
        }
        return msg;
    }

    public String validateNIC(String nic) {
        String regex = "^([0-9]{9}[x|X|v|V]|[0-9]{12})$";
        String msg = "";
        if (!nic.matches(regex)) {
            LOGGER.log(Level.WARNING, "Invalid NIC...");
            msg = "Invalid NIC";
        }
        return msg;
    }

    public String validateEmail(String email){
        String regex = "^(.+)@(.+)$";
        String msg = "";
        if (!email.matches(regex)) {
            LOGGER.log(Level.WARNING, "Invalid Email...");
            msg = "Invalid Email";
        }
        return msg;
    }

    public String validateAmount(String amount){
        String regex = "^[1-9]\\d{0,7}(?:\\.\\d{1,2})?$";
        String msg = "";
        if (!amount.matches(regex)){
            LOGGER.log(Level.WARNING, "Invalid Amount...");
            msg = "Invalid Amount";
        }
        return msg;
    }
}
