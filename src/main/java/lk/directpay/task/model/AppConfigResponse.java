package lk.directpay.task.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;


@NoArgsConstructor
@Data
public class AppConfigResponse {

    private String platform;
    private String latestVersion;
    private String appUrl;
    private String splashScreen;
    private String authScreen;
    private ArrayList countries;

}
