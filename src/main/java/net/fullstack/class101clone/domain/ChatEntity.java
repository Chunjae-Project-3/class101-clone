package net.fullstack.class101clone.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@SuperBuilder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "tbl_chat")
public class ChatEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(length = 50, nullable = false, columnDefinition = "varchar(50) comment '보낸 사람 ID'")
    private String sender;

    @Column(length = 50, nullable = false, columnDefinition = "varchar(50) comment '받는 사람 ID'")
    private String receiver;

    @Column(columnDefinition = "text comment '메시지 내용'", nullable = false)
    private String content;

    @Column(columnDefinition = "datetime comment '전송 시간'", nullable = false)
    private LocalDateTime timestamp;
}
