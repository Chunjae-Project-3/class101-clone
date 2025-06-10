package net.fullstack.class101clone.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "tbl_class_like")
public class ClassLikeEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "int(11) not null comment '클래스 찜 인덱스'")
    private int classLikeIdx;

    @ManyToOne
    @JoinColumn(name = "class_like_ref_idx", foreignKey = @ForeignKey(name = "FK_tbl_class_like_tbl_class"))
    private ClassEntity classLikeRef;

    @ManyToOne
    @JoinColumn(name = "class_like_user_id", foreignKey = @ForeignKey(name = "FK_tbl_class_like_tbl_user"))
    private UserEntity classLikeUser;
}
