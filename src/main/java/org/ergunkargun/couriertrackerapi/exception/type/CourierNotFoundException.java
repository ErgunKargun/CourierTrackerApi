package org.ergunkargun.couriertrackerapi.exception.type;

import org.ergunkargun.couriertrackerapi.exception.NotFoundException;

public class CourierNotFoundException extends NotFoundException {
    public CourierNotFoundException(Long id) {
        super("Could not find the courier with this id: " + id);
    }
}
