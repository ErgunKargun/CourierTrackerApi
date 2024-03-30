package org.ergunkargun.couriertrackerapi.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("api")
public class ApiProperties {

    private int circumferenceInMeters;

    private int intervalInMinutes;

    private String userName;

    private String userPassword;
}
