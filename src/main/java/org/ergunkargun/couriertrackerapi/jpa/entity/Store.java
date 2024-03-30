package org.ergunkargun.couriertrackerapi.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.*;
import org.ergunkargun.couriertrackerapi.jpa.entity.abstraction.AuditableEntity;
import org.ergunkargun.couriertrackerapi.jpa.entity.record.Coordinate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Store extends AuditableEntity<Long> {

    @Column(nullable = false)
    private String name;

    @Embedded
    private Coordinate coordinate;
}
