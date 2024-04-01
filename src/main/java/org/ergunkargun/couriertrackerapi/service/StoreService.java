package org.ergunkargun.couriertrackerapi.service;

import org.apache.commons.lang3.SerializationUtils;
import org.ergunkargun.couriertrackerapi.exception.type.StoreNotFoundException;
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

    public List<Store> create(List<Store> stores) {
        return storeRepo.saveAll(stores);
    }

    public Store create(Store store) {
        return storeRepo.save(store);
    }

    @Cacheable(value = "stores")
    public List<Store> read() {
        return storeRepo.findAll();
    }

    @CachePut(value = "stores")
    public List<Store> readAndPut() {
        return read();
    }

    public Store read(Long id) {
        return storeRepo.findById(id).orElseThrow(() -> new StoreNotFoundException(id));
    }

    public Store update(Store store) {
        return storeRepo.findById(store.getId())
                .map(user -> {
                    user = SerializationUtils.clone(store);
                    return storeRepo.save(user);
                })
                .orElseGet(() -> storeRepo.save(store));
    }

    public void delete() {
        storeRepo.deleteAll();
    }

    public void delete(Long id) {
        storeRepo.deleteById(id);
    }
}
