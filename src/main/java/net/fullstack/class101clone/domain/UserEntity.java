package net.fullstack.class101clone.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@SuperBuilder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "classLikeList", "lectureHistoryList" })
@Table(name = "tbl_user")
public class UserEntity extends BaseEntity {
    @Id
    @Column(name = "user_id", columnDefinition = "varchar(20) not null comment '회원 아이디'")
    private String userId;

    @Column(name = "user_pwd",columnDefinition = "varchar(255) not null comment '회원 비밀번호'")
    private String userPwd;

    @Column(name = "user_name",columnDefinition = "varchar(50) not null comment '회원 이름'")
    private String userName;

    // 양방향 관계 설정
    @OneToMany(mappedBy = "classLikeUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClassLikeEntity> classLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "lectureHistoryUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LectureHistoryEntity> lectureHistoryList = new ArrayList<>();
}
