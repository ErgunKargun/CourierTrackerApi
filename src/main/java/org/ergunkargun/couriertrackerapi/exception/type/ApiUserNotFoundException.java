package org.ergunkargun.couriertrackerapi.exception.type;

import org.ergunkargun.couriertrackerapi.exception.NotFoundException;

public class ApiUserNotFoundException extends NotFoundException {
    public ApiUserNotFoundException(Long id) {
        super("Could not find the api user with this id: " + id);
    }
}
