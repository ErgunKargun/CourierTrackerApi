package org.ergunkargun.couriertrackerapi.exception;

public abstract class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
