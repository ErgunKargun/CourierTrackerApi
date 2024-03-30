package org.ergunkargun.couriertrackerapi.jpa.entity;

import jakarta.persistence.Entity;
import lombok.*;
import org.ergunkargun.couriertrackerapi.jpa.entity.abstraction.PersistenceEntity;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Entrance extends PersistenceEntity<Long> {

    private Long storeId;

    private Long courierId;

    private LocalDateTime time;
}
