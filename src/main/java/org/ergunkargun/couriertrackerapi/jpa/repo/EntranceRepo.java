package org.ergunkargun.couriertrackerapi.jpa.repo;

import org.ergunkargun.couriertrackerapi.jpa.entity.Entrance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EntranceRepo extends JpaRepository<Entrance, Long> {
    Optional<Entrance> findEntranceByCourierIdAndStoreIdAndTimeAfter(Long courierId, Long storeId, LocalDateTime time);
}
