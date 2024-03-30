package org.ergunkargun.couriertrackerapi.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {

    private String secretKey = "m!gr0s";

    private long expirationDuration = 3600000; // 1 hour

}