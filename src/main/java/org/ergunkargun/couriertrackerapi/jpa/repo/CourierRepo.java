package org.ergunkargun.couriertrackerapi.jpa.repo;

import org.ergunkargun.couriertrackerapi.jpa.entity.Courier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourierRepo extends JpaRepository<Courier, Long> {
}
