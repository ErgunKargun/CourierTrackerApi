package org.ergunkargun.couriertrackerapi.service;

import org.ergunkargun.couriertrackerapi.exception.type.CourierNotFoundException;
import org.ergunkargun.couriertrackerapi.jpa.entity.Courier;
import org.ergunkargun.couriertrackerapi.jpa.repo.CourierRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourierService {

    private final CourierRepo courierRepo;

    public CourierService(CourierRepo courierRepo) {
        this.courierRepo = courierRepo;
    }

    public Courier create(Courier courier) {
        return courierRepo.save(courier);
    }

    public List<Courier> read() {
        return courierRepo.findAll();
    }

    public Courier read(Long id) {
        return courierRepo.findById(id).orElseThrow(() -> new CourierNotFoundException(id));
    }

    public Courier update(Courier courier) {
        return courierRepo.findById(courier.getId())
                .map(c -> {
                    c.setCoordinate(courier.getCoordinate());
                    c.setTime(courier.getTime());
                    c.setDistance(courier.getDistance());
                    return courierRepo.save(c);
                })
                .orElseGet(() -> courierRepo.save(courier));
    }

    public void delete() {
        courierRepo.deleteAll();
    }

    public void delete(Long id) {
        courierRepo.deleteById(id);
    }
}
