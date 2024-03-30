package org.ergunkargun.couriertrackerapi.jpa.repo;

import org.ergunkargun.couriertrackerapi.jpa.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepo extends JpaRepository<Store, Long> {
}
