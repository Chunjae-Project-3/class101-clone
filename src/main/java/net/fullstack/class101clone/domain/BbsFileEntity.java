package net.fullstack.class101clone.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_board_files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "board")
public class BbsFileEntity implements Comparable<BbsFileEntity> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private Long idx;

    private String uuid;

    private String fileName;

    private String fileType;

    private long fileSize;

    private int ord;

    private boolean imageFlag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_idx")
    private BbsEntity board;

    @Override
    public int compareTo(BbsFileEntity o) {
        return Integer.compare(this.ord, o.ord);
    }

    public void changeBoardFileEntity(BbsEntity board) {
        this.board = board;
    }
}
