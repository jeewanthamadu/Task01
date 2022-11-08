package lk.directpay.task.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Entity
public class AppConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String platform;
    private String minVersion;
    private String latestVersion;
    private String appUrl;
    private String splashScreen;
    private String authScreen;

}
