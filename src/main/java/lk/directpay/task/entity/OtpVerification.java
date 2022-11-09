package lk.directpay.task.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;


@NoArgsConstructor
@Getter
@Setter
@Entity
public class OtpVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String email;
    private String mobile;
    private String otp;
    private int attempts;
    private LocalDateTime createdAt;
    private String mobileVerified;
    private String emailVerified;
    private int otpAttempts;


}
