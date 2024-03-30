package org.ergunkargun.couriertrackerapi.configuration.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api")
@Data
public class ApiProperties {
    @Value( "${circumferenceInMeters:100}" )
    public int circumferenceInMeters;

    @Value( "${intervalInMinutes:1}" )
    public int intervalInMinutes;
}
