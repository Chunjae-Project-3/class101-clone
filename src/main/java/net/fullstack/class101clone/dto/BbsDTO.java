package net.fullstack.class101clone.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class BbsDTO {
    private Long idx;
    private String title;
    private String content;
    private String user_id;
    private String display_date;
    private int read_cnt;
}