package org.ergunkargun.couriertrackerapi.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.ergunkargun.couriertrackerapi.jpa.entity.ApiUser;
import org.ergunkargun.couriertrackerapi.jpa.entity.Store;
import org.ergunkargun.couriertrackerapi.jpa.entity.enumaration.Role;
import org.ergunkargun.couriertrackerapi.observe.CourierLogEventListener;
import org.ergunkargun.couriertrackerapi.service.ApiUserService;
import org.ergunkargun.couriertrackerapi.service.StoreService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cglib.core.internal.Function;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Configuration
public class ApiConfiguration {

    private final Environment environment;

    public ApiConfiguration(Environment environment) {
        this.environment = environment;
    }

    @Bean
    CommandLineRunner initialize(ApiUserService apiUserService, PasswordEncoder passwordEncoder, StoreService storeService) {
        return args -> {
            initializeApiUsers(apiUserService, passwordEncoder);
            initializeStores(storeService);
        };
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

        apiUserService.read().forEach(user -> log.debug(String.format("User %s created", user.toString())));
    }

    private void initializeStores(StoreService storeService) {
        InputStream storeData = TypeReference.class.getResourceAsStream("/data/stores-coordinates.json");
        try {
            List<Store> stores = new ObjectMapper().readValue(storeData, new TypeReference<>() {
            });
            storeService.create(stores);
        } catch (IOException e) {
            log.debug("Unable to save stores. " + e.getMessage());
        }
    }
}
