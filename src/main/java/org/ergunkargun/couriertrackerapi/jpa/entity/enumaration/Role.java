package org.ergunkargun.couriertrackerapi.jpa.entity.enumaration;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    ADMIN,
    USER;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
