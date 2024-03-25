package org.ergunkargun.couriertrackerapi.service.observe;

import org.ergunkargun.couriertrackerapi.jpa.entity.Entrance;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

public class EntranceEvent extends ApplicationEvent {

    private final Entrance entrance;

    public EntranceEvent(Object source, Entrance entrance) {
        super(source, Clock.systemUTC());
        this.entrance = entrance;
    }

    public Entrance getEntrance() {
        return entrance;
    }
}
