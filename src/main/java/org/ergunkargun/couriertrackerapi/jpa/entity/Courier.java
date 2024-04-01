package org.ergunkargun.couriertrackerapi.jpa.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private LocalDateTime time;

    @Builder.Default
    private double distance = 0.0d;
}
