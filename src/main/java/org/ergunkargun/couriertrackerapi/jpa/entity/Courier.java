package org.ergunkargun.couriertrackerapi.jpa.entity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.*;
import org.ergunkargun.couriertrackerapi.jpa.entity.abstraction.PersistenceEntity;
import org.ergunkargun.couriertrackerapi.jpa.entity.record.Coordinate;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Courier extends PersistenceEntity<Long> {

    @Embedded
    private Coordinate coordinate;

    private LocalDateTime time;

    @Builder.Default
    private double distance = 0.0d;
}
