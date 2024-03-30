package org.ergunkargun.couriertrackerapi.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CacheService {

    private final StoreService storeService;

    public CacheService(StoreService storeService) {
        this.storeService = storeService;
    }

    @Scheduled(cron = "0 0 0/1 1/1 * ?")//every hour
    public void refreshStores() {
        var stores = storeService.readAndPut();
        log.info(String.format("Stores cache refreshed. Size: %s", stores.size()));
    }
}
