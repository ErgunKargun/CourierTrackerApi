package org.ergunkargun.couriertrackerapi.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.ergunkargun.couriertrackerapi.jpa.entity.ApiUser;
import org.ergunkargun.couriertrackerapi.jpa.entity.Store;
import org.ergunkargun.couriertrackerapi.jpa.entity.enumaration.Role;
import org.ergunkargun.couriertrackerapi.service.ApiUserService;
import org.ergunkargun.couriertrackerapi.service.StoreService;
import org.ergunkargun.couriertrackerapi.service.observe.CourierLogEventListener;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cglib.core.internal.Function;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Configuration
public class ApiConfiguration {

    @Bean
    public Function<Store, CourierLogEventListener> storeFactory() {
        return this::createStore;
    }

    @Bean
    @Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public CourierLogEventListener createStore(Store store) {
        return new CourierLogEventListener(store);
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
                .username("admin")
                .password(passwordEncoder.encode("m!gr0s"))
                .role(Role.ADMIN)
                .build()
        );

        apiUserService.create(ApiUser.builder()
                .username("admin")
                .password(passwordEncoder.encode("m!gr0s"))
                .role(Role.ADMIN)
                .build()
        );

        log.debug("Printing Users");
        apiUserService.read().forEach(user -> log.debug(String.format("User : %s ", user.toString())));
    }

    private void initializeStores(StoreService storeService) {
        InputStream storeData = TypeReference.class.getResourceAsStream("/data/stores-coordinates.json");
        try {
            List<Store> stores = new ObjectMapper().readValue(storeData, new TypeReference<>() {
            });
            storeService.create(stores);
            stores.forEach(store -> storeFactory().apply(store));
        } catch (IOException e) {
            log.debug("Unable to save stores. " + e.getMessage());
        }
    }
}
