package org.ergunkargun.couriertrackerapi.service;

import org.ergunkargun.couriertrackerapi.jpa.entity.Entrance;
import org.ergunkargun.couriertrackerapi.jpa.repo.EntranceRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EntranceService {

    final EntranceRepo entranceRepo;

    public EntranceService(EntranceRepo entranceRepo) {
        this.entranceRepo = entranceRepo;
    }

    public void create(Entrance entrance) {
        entranceRepo.save(entrance);
    }

    public List<Entrance> read() {
        return entranceRepo.findAll();
    }

    public Optional<Entrance> read(Long courierId, Long storeId, int intervalInMinutes) {
        var timeAfter = LocalDateTime.now().minusMinutes(intervalInMinutes);
        return entranceRepo.findEntranceByCourierIdAndStoreIdAndTimeAfter(courierId, storeId, timeAfter);
    }

    public void delete() {
        entranceRepo.deleteAll();
    }
}
