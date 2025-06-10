package net.fullstack.class101clone.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class BbsListWithFileDTO {
    private Long idx;
    @Size(min = 1, max = 200, message = "게시글 제목은 1자 이상, 200자 이하여야 합니다.")
    private String title;
    @Size(min = 1, message = "게시글 내용은 1자 이상이어야 합니다.")
    private String content;
    @NotBlank
    private String user_id;
    private String display_date;
    @Min(1)
    private int read_cnt;
    private long reply_count;
    private String reg_date;

    private List<BbsFilesDTO> bbs_files;
}