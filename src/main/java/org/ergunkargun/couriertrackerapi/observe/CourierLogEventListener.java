package org.ergunkargun.couriertrackerapi.observe;

import lombok.extern.slf4j.Slf4j;
import org.ergunkargun.couriertrackerapi.configuration.properties.ApiProperties;
import org.ergunkargun.couriertrackerapi.helper.CoordinateUtil;
import org.ergunkargun.couriertrackerapi.jpa.entity.Courier;
import org.ergunkargun.couriertrackerapi.jpa.entity.Entrance;
import org.ergunkargun.couriertrackerapi.jpa.entity.Store;
import org.ergunkargun.couriertrackerapi.service.CourierService;
import org.ergunkargun.couriertrackerapi.service.EntranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

import java.time.LocalDateTime;

@Slf4j
public class CourierLogEventListener {

    @Autowired
    private ApiProperties apiProperties;

    @Autowired
    private CoordinateUtil coordinateUtil;

    @Autowired
    private EntranceService entranceService;

    @Autowired
    private CourierService courierService;

    private final Store store;

    public CourierLogEventListener(Store store) {
        this.store = store;
    }

    @EventListener
    public void handleCourierLogEvent(CourierLogEvent event) {

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
