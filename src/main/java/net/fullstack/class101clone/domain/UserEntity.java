package net.fullstack.class101clone.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "tbl_user")
public class UserEntity extends BaseEntity {
    @Id
    @Column(columnDefinition = "varchar(20) not null comment '회원 아이디'")
    private String userId;
    @Column(columnDefinition = "varchar(255) not null comment '회원 비밀번호'")
    private String userPwd;
    @Column(columnDefinition = "varchar(50) not null comment '회원 이름'")
    private String userName;
}
