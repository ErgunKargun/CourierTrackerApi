package org.ergunkargun.couriertrackerapi.observe;

import jakarta.annotation.PostConstruct;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.ergunkargun.couriertrackerapi.configuration.api.ApiProperties;
import org.ergunkargun.couriertrackerapi.helper.CoordinateUtil;
import org.ergunkargun.couriertrackerapi.jpa.entity.Courier;
import org.ergunkargun.couriertrackerapi.jpa.entity.Entrance;
import org.ergunkargun.couriertrackerapi.jpa.entity.Store;
import org.ergunkargun.couriertrackerapi.service.CourierService;
import org.ergunkargun.couriertrackerapi.service.EntranceService;
import org.ergunkargun.couriertrackerapi.service.StoreService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;

import java.time.LocalDateTime;

@Slf4j
@Setter
public class CourierLogEventListener implements ApplicationListener<CourierLogEvent> {

    private ApiProperties apiProperties;

    private CoordinateUtil coordinateUtil;

    private EntranceService entranceService;

    private CourierService courierService;

    private StoreService storeService;

    private Store store;

    @PostConstruct
    public void init(){
        log.info((store != null ? store.getName() : "null store") + " bean initialized");
    }

    @Override
    @EventListener
    public void onApplicationEvent(CourierLogEvent event) {

        if (store == null)
            return;

        Courier courier = event.getCourier();

        var distance = coordinateUtil.distanceBetween(store.getCoordinate(), courier.getCoordinate());

        courier.setDistance(courier.getDistance() + distance);

        courierService.update(courier);

        if (distance > apiProperties.getCircumferenceInMeters())
            return;

        var entrance = entranceService.read(courier.getId(), store.getId(), apiProperties.getIntervalInMinutes());

        if (entrance.isEmpty()) {
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

    @Override
    public boolean supportsAsyncExecution() {
        return true;
    }

    public void saveStore() {
        storeService.create(store);
    }
}
