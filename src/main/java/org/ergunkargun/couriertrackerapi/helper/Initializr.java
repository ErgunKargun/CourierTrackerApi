package org.ergunkargun.couriertrackerapi.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.ergunkargun.couriertrackerapi.jpa.entity.Store;
import org.ergunkargun.couriertrackerapi.observe.CourierLogEventListener;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
public class Initializr implements ApplicationContextInitializer<GenericApplicationContext> {

    @Override
    public void initialize(GenericApplicationContext applicationContext) {

        try {
            InputStream storeData = TypeReference.class.getResourceAsStream("/data/stores-coordinates.json");
            List<Store> storeList = new ObjectMapper().readValue(storeData, new TypeReference<>() {
            });
            for (var store : storeList) {
                var storeBean = new CourierLogEventListener();
                storeBean.setStore(store);
                applicationContext.registerBean("store-"+store.getName(), CourierLogEventListener.class, () -> storeBean);
            }
        } catch (IOException e) {
            log.debug("Unable to save stores. " + e.getMessage());
        }
    }
}
