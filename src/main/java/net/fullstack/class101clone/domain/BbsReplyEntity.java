package net.fullstack.class101clone.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "board")
@Table(name = "tbl_board_reply",
        indexes = {@Index(name = "IDX_tbl_board_reply_board_idx", columnList = "board_idx")})
public class BbsReplyEntity extends BbsBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private long idx;

    // 지연에 대한 참조
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "board_idx", nullable = false)
    private BbsEntity board;

    @Column(columnDefinition = "VARCHAR(20) NOT NULL COMMENT '아이디' COLLATE 'utf8mb4_unicode_ci'")
    private String reply_user_id;
    @Column(columnDefinition = "VARCHAR(2000) NOT NULL COMMENT '댓글 내용' COLLATE 'utf8mb4_unicode_ci'")
    private String reply_content;
    @CreatedDate
    @Column(name = "reply_date", nullable = false, updatable = false, columnDefinition = "DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '댓글 등록일'")
    private LocalDateTime reply_date;
    @LastModifiedDate
    @Column(name = "reply_modify_date", nullable = true, insertable = false, updatable = true, columnDefinition = "DATETIME NULL DEFAULT NULL COMMENT '댓글 수정일'")
    private LocalDateTime reply_modify_date;
}
