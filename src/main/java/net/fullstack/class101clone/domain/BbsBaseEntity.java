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
public abstract class BbsBaseEntity {
    @CreatedDate
    @Column(name = "reg_date", updatable = false, columnDefinition = "datetime not null default current_timestamp comment '등록일'")
    private LocalDateTime reg_date;

    @Setter
    @LastModifiedDate
    @Column(name = "modify_date", nullable = true, insertable = false, updatable = true, columnDefinition = "datetime null default current_timestamp comment '수정일'")
    private LocalDateTime modify_date;
}