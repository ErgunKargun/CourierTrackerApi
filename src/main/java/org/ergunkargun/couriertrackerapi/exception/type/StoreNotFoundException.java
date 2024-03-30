package org.ergunkargun.couriertrackerapi.exception.type;

import org.ergunkargun.couriertrackerapi.exception.NotFoundException;

public class StoreNotFoundException extends NotFoundException {
    public StoreNotFoundException(Long id) {
        super("Could not find the store with this id: " + id);
    }
}
