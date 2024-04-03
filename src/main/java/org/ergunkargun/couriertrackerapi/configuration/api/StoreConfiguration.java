package org.ergunkargun.couriertrackerapi.configuration.api;

import lombok.extern.slf4j.Slf4j;
import org.ergunkargun.couriertrackerapi.helper.CoordinateUtil;
import org.ergunkargun.couriertrackerapi.observe.CourierLogEventListener;
import org.ergunkargun.couriertrackerapi.service.CourierService;
import org.ergunkargun.couriertrackerapi.service.EntranceService;
import org.ergunkargun.couriertrackerapi.service.StoreService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class StoreConfiguration implements BeanPostProcessor {

    private final ApiProperties apiProperties;

    private final CoordinateUtil coordinateUtil;

    private final EntranceService entranceService;

    private final CourierService courierService;

    private final StoreService storeService;

    public StoreConfiguration(ApiProperties apiProperties, CoordinateUtil coordinateUtil, EntranceService entranceService, CourierService courierService, StoreService storeService) {
        this.apiProperties = apiProperties;
        this.coordinateUtil = coordinateUtil;
        this.entranceService = entranceService;
        this.courierService = courierService;
        this.storeService = storeService;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if (bean.getClass().equals(CourierLogEventListener.class)) {
            var storeBean = (CourierLogEventListener) bean;
            storeBean.setApiProperties(apiProperties);
            storeBean.setCoordinateUtil(coordinateUtil);
            storeBean.setEntranceService(entranceService);
            storeBean.setCourierService(courierService);
            storeBean.setStoreService(storeService);
            storeBean.saveStore();

            log.info(String.format("%s bean set successfully", beanName));
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
