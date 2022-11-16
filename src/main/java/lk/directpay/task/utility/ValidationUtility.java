package lk.directpay.task.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class ValidationUtility {

    private final Validations validations;

    @Autowired
    public ValidationUtility(Validations validations) {
        this.validations = validations;
    }

    public String required(HashMap<String, Object> payload, String... params) {
        for (String param : params) {
            if (!payload.containsKey(param)) {
                return param + " required!";
            }
            switch (param) {
                case "mobile":
                    if (payload.get("mobile").equals("")) {
                        return Translator.toLocale("mobile_null_err");
                    } else {
                        String error = validations.validateMobile(payload.get("mobile").toString());
                        if (error.equals("")) {
                            continue;
                        } else {
                            return error;
                        }
                    }
                case "firstname":
                    if (payload.get("firstname").equals("")) {
                        return Translator.toLocale("first_name_null_err");

                    } else {
                        String error = validations.validateName(payload.get("firstname").toString());
                        if (error.equals("")) {
                            continue;
                        } else {
                            return error;
                        }
                    }
                case "lastname":
                    if (payload.get("lastname").equals("")) {
                        return Translator.toLocale("last_name_null_err");
                    } else {
                        String error = validations.validateName(payload.get("lastname").toString());
                        if (error.equals("")) {
                            continue;
                        } else {
                            return error;
                        }
                    }
                case "nic":
                    if (payload.get("nic").equals("")) {
                        return Translator.toLocale("nic_null_err");
                    } else {
                        String error = validations.validateNIC(payload.get("nic").toString());
                        if (error.equals("")) {
                            continue;
                        } else {
                            return error;
                        }
                    }
                case "email":
                    if (payload.get("email").equals("")) {
                        return Translator.toLocale("email_null_err");
                    } else {
                        String error = validations.validateEmail(payload.get("email").toString());
                        if (error.equals("")) {
                            continue;
                        } else {
                            return error;
                        }
                    }
                case "amount":
                    if (payload.get("amount").equals("")){
                        return Translator.toLocale("amount_null_err");
                    }else{
                        String error = validations.validateAmount(payload.get("amount").toString());
                        if (error.equals("")) {
                            continue;
                        } else {
                            return error;
                        }
                    }
            }
        }
        return null;
    }

}
