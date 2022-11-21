package lk.directpay.task.model;


import lk.directpay.task.utility.Translator;
import org.springframework.context.NoSuchMessageException;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class DefaultResponse {
    private final static Logger LOGGER = Logger.getLogger("RESPONSE");

    private int status = 400;
    private String title;
    private String message;
    private Map<String, Object> data = new HashMap<>();

    public DefaultResponse() {
    }

    public static DefaultResponse success(String title_code, String message_code) {
        try {
            String title = Translator.toLocale(title_code);
            String message = Translator.toLocale(message_code);
            return new DefaultResponse(200, title, message);
        } catch (NoSuchMessageException e) {
            return new DefaultResponse(200, title_code, message_code);
        }
    }

    public static DefaultResponse success(String title, String message, Map<String, Object> data) {
        return new DefaultResponse(200, title, message, data);
    }

    public static DefaultResponse array(String arrayName, Object array) {
        Map<String, Object> data = new HashMap<>();
        data.put(arrayName, array);
        return new DefaultResponse(200, "Data Array", "Data Array", data);
    }

    public static DefaultResponse error() {
        String title = Translator.toLocale("unexpected_error");
        String message = Translator.toLocale("unexpected_error_msg");
        return new DefaultResponse(400, title, message);
    }

    public static DefaultResponse error(String title_code, String message_code) {
        try {
            String title = Translator.toLocale(title_code);
            String message = Translator.toLocale(message_code);
            return new DefaultResponse(400, title, message);
        } catch (NoSuchMessageException e) {
            return new DefaultResponse(400, title_code, message_code);
        }
    }

    public static DefaultResponse error(String title, String message, Map<String, Object> data) {

        return new DefaultResponse(400, title, message, data);

    }

    public DefaultResponse(int status, String title, String message, Map<String, Object> data) {
        this.status = status;
        this.title = title;
        this.message = message;
        this.data = data;
    }

    public DefaultResponse(int status, String title, String message) {

        this.status = status;
        this.title = title;
        this.message = message;
        this.data = new HashMap<>();


    }

    public static DefaultResponse getInstance(int status, String title, String message, Map<String, Object> data) {
        return new DefaultResponse(status, title, message, data);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public DefaultResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
