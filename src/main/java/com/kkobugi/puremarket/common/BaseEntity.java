package com.kkobugi.puremarket.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @Column(columnDefinition = "varchar(10) default 'active'")
    @Setter
    private String status;

    @CreatedDate
    @Column(updatable = false)
    private LocalDate createdDate;

    @LastModifiedDate
    private LocalDate lastModifiedDate;
}
