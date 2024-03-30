package org.ergunkargun.couriertrackerapi.service;

import org.apache.commons.lang3.SerializationUtils;
import org.ergunkargun.couriertrackerapi.exception.type.ApiUserNotFoundException;
import org.ergunkargun.couriertrackerapi.jpa.entity.ApiUser;
import org.ergunkargun.couriertrackerapi.jpa.repo.ApiUserRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiUserService {

    private final ApiUserRepo apiUserRepo;

    public ApiUserService(ApiUserRepo apiUserRepo) {
        this.apiUserRepo = apiUserRepo;
    }

    public ApiUser create(ApiUser ApiUser) {
        return apiUserRepo.save(ApiUser);
    }

    public List<ApiUser> read() {
        return apiUserRepo.findAll();
    }

    public ApiUser read(Long id) {
        return apiUserRepo.findById(id).orElseThrow(() -> new ApiUserNotFoundException(id));
    }

    public ApiUser update(ApiUser apiUser) {
        return apiUserRepo.findById(apiUser.getId())
                .map(user -> {
                    user = SerializationUtils.clone(apiUser);
                    return apiUserRepo.save(user);
                })
                .orElseGet(() -> apiUserRepo.save(apiUser));
    }

    public void delete() {
        apiUserRepo.deleteAll();
    }

    public void delete(Long id) {
        apiUserRepo.deleteById(id);
    }
}
