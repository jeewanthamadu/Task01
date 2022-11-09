package lk.directpay.task.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class AppUser {
    public static final int LOGIN_METHOD_PIN = 0;
    public static final int LOGIN_METHOD_BIOMETRIC = 1;

    public static final int ACTIVE = 1;
    public static final int DISABLED = 0;
    public static final int TEMPORARY_ACTIVE = 2;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private int id;

    @Column(unique = true)
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String nic;
    private String nicFront;
    private String nicBack;
    @Column(columnDefinition = "integer default 1")
    private int countryId;
    private String phoneNumber;
    private String password;
    private String biometricChallenge;
    @Column(columnDefinition = "integer default 0")
    private int loginMethod;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;
    private boolean mobileVerified;
    private boolean emailVerified;
    private byte otpAttempts;
    private String deviceId;
    private String appVersion;
    private String justPayUserId;
    private String fcmToken;
    @Column(columnDefinition = "integer default 0")
    private int loginAttempts;
    @Column(columnDefinition = "integer default 1")
    private int status;
    @Column(length = 2048)
    private String publicKey;
    private LocalDateTime createdAt;
    private String disabledReason;
    private String defaultTillId;

    public AppUser() {
        this.createdAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getPassword() {
        if (getLoginMethod() == LOGIN_METHOD_BIOMETRIC) {
            return biometricChallenge;
        }
        return password;
    }

    public String getPIN() {
        return password;
    }

    public void setPassword(String password) {
        this.password = new BCryptPasswordEncoder().encode(password);
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getBiometricChallenge() {
        return biometricChallenge;
    }

    public void setBiometricChallenge(String biometricChallenge) {
        this.biometricChallenge = biometricChallenge == null ? null
                : new BCryptPasswordEncoder().encode(biometricChallenge);
    }

    public int getLoginMethod() {
        return loginMethod;
    }

    public void setLoginMethod(int loginMethod) {
        this.loginMethod = loginMethod;
    }

    public Collection<GrantedAuthority> getGrantedAuthoritiesList() {
        return grantedAuthoritiesList;
    }

    public void setGrantedAuthoritiesList(Collection<GrantedAuthority> grantedAuthoritiesList) {
        this.grantedAuthoritiesList = grantedAuthoritiesList;
    }

    public String getFullName() {
        return this.getFirstname() + " " + this.getLastname();
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

    public String getName(int maxLength) {
        String name = this.firstname + " " + this.lastname;

        if (name.length() > maxLength) {
            return name.substring(0, maxLength);
        }
        return name;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getNicFront() {
        return nicFront;
    }

    public void setNicFront(String nicFront) {
        this.nicFront = nicFront;
    }

    public String getNicBack() {
        return nicBack;
    }

    public void setNicBack(String nicBack) {
        this.nicBack = nicBack;
    }

    public boolean isMobileVerified() {
        return mobileVerified;
    }

    public void setMobileVerified(boolean mobileVerified) {
        this.mobileVerified = mobileVerified;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public byte getOtpAttempts() {
        return otpAttempts;
    }

    public void setOtpAttempts(byte otpAttempts) {
        this.otpAttempts = otpAttempts;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    @Transient
    private Collection<GrantedAuthority> grantedAuthoritiesList = new ArrayList<>();

    /**
     * @return String return the justPayUserId
     */
    public String getJustPayUserId() {
        return justPayUserId;
    }

    /**
     * @param justPayUserId the justPayUserId to set
     */
    public void setJustPayUserId(String justPayUserId) {
        this.justPayUserId = justPayUserId;
    }

    public int getLoginAttempts() {
        return loginAttempts;
    }

    public void setLoginAttempts(int loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getDisabledReason() {
        return disabledReason;
    }

    public void setDisabledReason(String disabledReason) {
        this.disabledReason = disabledReason;
    }

    public String getDefaultTillId() {
        return defaultTillId;
    }

    public void setDefaultTillId(String defaultTillId) {
        this.defaultTillId = defaultTillId;
    }

    public boolean isNICRequired() {
        return this.status == DISABLED && this.getDisabledReason().toLowerCase().contains("nic");
    }
}