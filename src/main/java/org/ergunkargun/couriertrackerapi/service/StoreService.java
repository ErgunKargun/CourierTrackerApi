package org.ergunkargun.couriertrackerapi.service;

import org.ergunkargun.couriertrackerapi.jpa.entity.Store;
import org.ergunkargun.couriertrackerapi.jpa.repo.StoreRepo;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService {

    private final StoreRepo storeRepo;

    public StoreService(StoreRepo storeRepo) {
        this.storeRepo = storeRepo;
    }

    public void create(List<Store> stores) {
        storeRepo.saveAll(stores);
    }

    @Cacheable(value = "stores")
    public List<Store> read() {
        return storeRepo.findAll();
    }

    @CachePut(value = "stores")
    public List<Store> readAndPut() {
        return read();
    }

    public void delete() {
        storeRepo.deleteAll();
    }
}
