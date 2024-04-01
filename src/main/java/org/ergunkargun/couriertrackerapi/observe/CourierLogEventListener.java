package org.ergunkargun.couriertrackerapi.observe;

import lombok.extern.slf4j.Slf4j;
import org.ergunkargun.couriertrackerapi.configuration.properties.ApiProperties;
import org.ergunkargun.couriertrackerapi.helper.CoordinateUtil;
import org.ergunkargun.couriertrackerapi.jpa.entity.Courier;
import org.ergunkargun.couriertrackerapi.jpa.entity.Entrance;
import org.ergunkargun.couriertrackerapi.jpa.entity.Store;
import org.ergunkargun.couriertrackerapi.service.CourierService;
import org.ergunkargun.couriertrackerapi.service.EntranceService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CourierLogEventListener {

    private final ApiProperties apiProperties;

    private final CoordinateUtil coordinateUtil;

    private final EntranceService entranceService;

    private final CourierService courierService;

    public CourierLogEventListener(ApiProperties apiProperties, CoordinateUtil coordinateUtil, EntranceService entranceService, CourierService courierService) {
        this.apiProperties = apiProperties;
        this.coordinateUtil = coordinateUtil;
        this.entranceService = entranceService;
        this.courierService = courierService;
    }

    @EventListener
    public void handleCourierLogEvent(CourierLogEvent event) {

        Store store = event.getStore();

        Courier courier = event.getCourier();

        var distance = coordinateUtil.distanceBetween(store.getCoordinate(), courier.getCoordinate());

        courier.setDistance(courier.getDistance() + distance);

        courierService.update(courier);

        if (distance > apiProperties.getCircumferenceInMeters())
            return;

        var entrance = entranceService.read(courier.getId(), store.getId(), apiProperties.getIntervalInMinutes());

        if(entrance.isEmpty()) {
            entranceService.create(Entrance
                    .builder()
                    .courierId(courier.getId())
                    .storeId(store.getId())
                    .time(LocalDateTime.now())
                    .build());
        } else {
            log.info(String.format("Entrance is skipped cause an existing entrance found in the last %s minutes", apiProperties.getIntervalInMinutes()));
        }
    }
}
