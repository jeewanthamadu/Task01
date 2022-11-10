package lk.directpay.task.model;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OtpVerify {

    private String email;
    private String mobile;

}
