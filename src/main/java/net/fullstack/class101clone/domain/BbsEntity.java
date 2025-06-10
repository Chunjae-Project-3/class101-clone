package net.fullstack.class101clone.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "bbsFileEntitySet")
@Table(name = "tbl_board")
public class BbsEntity extends BbsBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", unique = true, nullable = false)
    private Long idx;
    @Column(columnDefinition = "varchar(200) not null")
    private String title;
    @Column(columnDefinition = "text not null")
    private String content;
    @Column(columnDefinition = "varchar(20) not null comment '아이디'")
    private String user_id;
    @Column(columnDefinition = "char(10) null comment '출력날짜'")
    private String display_date;
    @Builder.Default
    @Min(0)
    @Column(columnDefinition = "int null default 0")
    private int read_cnt = 0;

    public void modify(String title, String content, String user_id, String display_date) {
        this.title = title;
        this.content = content;
        this.user_id = user_id;
        this.display_date = display_date;
        super.setModify_date(LocalDateTime.now());
    }

    @Builder.Default
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @BatchSize(size = 20) // 20개씩 묶어서 한 번에 처리해라
    private Set<BbsFileEntity> bbsFileEntitySet = new HashSet<>();

    public void addBbsFiles(String uuid, String fileName) {
        BbsFileEntity bbsFileEntity = BbsFileEntity.builder()
                .uuid(uuid)
                .fileName(fileName)
                .board(this)
                .ord(bbsFileEntitySet.size() + 1)
                .build();
        bbsFileEntitySet.add(bbsFileEntity);
    }

    public void clearBbsFiles() {
        bbsFileEntitySet.clear();
    }
}