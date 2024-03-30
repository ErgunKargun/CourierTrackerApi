package org.ergunkargun.couriertrackerapi.jpa.entity.abstraction;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.ergunkargun.couriertrackerapi.jpa.entity.record.Username;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableEntity<ID extends Serializable> extends PersistenceEntity<ID> implements Serializable {

    @CreatedDate
    LocalDate createdDate;

    @CreatedBy
    @AttributeOverride(name = "username", column = @Column(name = "created_by"))
    @Embedded
    Username createdBy;

    @LastModifiedDate
    LocalDate lastModifiedDate;

    @LastModifiedBy
    @AttributeOverride(name = "username", column = @Column(name = "last_modified_by"))
    @Embedded
    Username lastModifiedBy;
}
