package org.ergunkargun.couriertrackerapi.observe;

import lombok.Getter;
import org.ergunkargun.couriertrackerapi.jpa.entity.Courier;
import org.ergunkargun.couriertrackerapi.jpa.entity.Store;
import org.springframework.context.ApplicationEvent;

@Getter
public class CourierLogEvent extends ApplicationEvent {

    private final Store store;

    private final Courier courier;

    public CourierLogEvent(Object source, Store store, Courier courier) {
        super(source);
        this.store = store;
        this.courier = courier;
    }

}
