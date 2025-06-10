package net.fullstack.class101clone.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder(toBuilder = true)
public class BaseEntity {
    @CreatedDate
    @Column(nullable = false, updatable = false, columnDefinition = "datetime not null default current_timestamp()")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(insertable = false, columnDefinition = "datetime null default null on update current_timestamp()")
    private LocalDateTime updatedAt;
}
