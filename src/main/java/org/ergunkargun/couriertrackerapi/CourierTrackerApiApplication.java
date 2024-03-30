package org.ergunkargun.couriertrackerapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class CourierTrackerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CourierTrackerApiApplication.class, args);
    }
}
