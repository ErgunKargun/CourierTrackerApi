package org.ergunkargun.couriertrackerapi.jpa.repo;

import org.ergunkargun.couriertrackerapi.jpa.entity.ApiUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiUserRepo extends JpaRepository<ApiUser, Long> {
    Optional<ApiUser> findByUsername(String username);
}
