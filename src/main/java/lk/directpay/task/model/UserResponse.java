package lk.directpay.task.model;

public class UserResponse {
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String nic;
    private String phoneNumber;
    private String deviceId;
    private String transactionAuthLimit;
    private String maxTransactionLimit;
    private String defaultTill;

    public UserResponse() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getTransactionAuthLimit() {
        return transactionAuthLimit;
    }

    public void setTransactionAuthLimit(String transactionAuthLimit) {
        this.transactionAuthLimit = transactionAuthLimit;
    }

    public String getMaxTransactionLimit() {
        return maxTransactionLimit;
    }

    public void setMaxTransactionLimit(String maxTransactionLimit) {
        this.maxTransactionLimit = maxTransactionLimit;
    }

    public String getDefaultTill() {
        return defaultTill;
    }

    public void setDefaultTill(String defaultTill) {
        this.defaultTill = defaultTill;
    }
}
