package lk.directpay.task.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppUserActivity {
    public final static int PENDING = 0;
    public final static int SUCCESS = 1;
    public final static int FAILED = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String action;
    private String username;
    private String appVersion;
    private String deviceId;
    private String email;
    private String mobile;
    private String nic;
    private String apiEndpoint;
    @Lob
    private String apiRequest;
    @Lob
    private String apiResponse;
    private String bankReference;
    private String apiStatusCode;
    private String apiStatusDescription;
    private String entity;
    private Integer linkedRecordId;
    private String description;
    private String requestId;
    private String ipAddress;

    @Column(columnDefinition = "integer default 0")
    private Integer status;

    private String userReference;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
