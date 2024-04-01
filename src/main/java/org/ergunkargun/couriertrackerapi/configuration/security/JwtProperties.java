package org.ergunkargun.couriertrackerapi.configuration.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("jwt")
public class JwtProperties {

    private String secretKey;

    private long expirationDurationInSeconds;

}