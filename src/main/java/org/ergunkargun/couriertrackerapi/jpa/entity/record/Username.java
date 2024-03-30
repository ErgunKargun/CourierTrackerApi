package org.ergunkargun.couriertrackerapi.jpa.entity.record;

import jakarta.persistence.Embeddable;

@Embeddable
public record Username(String username) {
}
