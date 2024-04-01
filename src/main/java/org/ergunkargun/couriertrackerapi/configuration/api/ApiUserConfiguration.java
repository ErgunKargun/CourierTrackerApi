package org.ergunkargun.couriertrackerapi.configuration.api;

import lombok.extern.slf4j.Slf4j;
import org.ergunkargun.couriertrackerapi.jpa.entity.ApiUser;
import org.ergunkargun.couriertrackerapi.jpa.entity.enumaration.Role;
import org.ergunkargun.couriertrackerapi.service.ApiUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
public class ApiUserConfiguration {

    private final Environment environment;

    public ApiUserConfiguration(Environment environment) {
        this.environment = environment;
    }

    @Bean
    CommandLineRunner initialize(ApiUserService apiUserService, PasswordEncoder passwordEncoder) {
        return args -> initializeApiUsers(apiUserService, passwordEncoder);
    }

    private void initializeApiUsers(ApiUserService apiUserService, PasswordEncoder passwordEncoder) {

        apiUserService.create(ApiUser.builder()
                .email("abc.admin@migrosone.com")
                .username(environment.getProperty("spring.security.user.name"))
                .password(passwordEncoder.encode(environment.getProperty("spring.security.user.password")))
                .role(Role.ADMIN)
                .build()
        );

        apiUserService.create(ApiUser.builder()
                .email("abc.user@migrosone.com")
                .username(environment.getProperty("api.user-name"))
                .password(passwordEncoder.encode(environment.getProperty("api.user-password")))
                .role(Role.USER)
                .build()
        );

        apiUserService.read().forEach(user -> log.info(String.format("User %s created", user.toString())));
    }

}
