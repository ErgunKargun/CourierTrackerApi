package org.ergunkargun.couriertrackerapi.observe;

import lombok.Getter;
import org.ergunkargun.couriertrackerapi.jpa.entity.Courier;
import org.springframework.context.ApplicationEvent;

@Getter
public class CourierLogEvent extends ApplicationEvent {

    private final Courier courier;

    public CourierLogEvent(Object source, Courier courier) {
        super(source);
        this.courier = courier;
    }

}
