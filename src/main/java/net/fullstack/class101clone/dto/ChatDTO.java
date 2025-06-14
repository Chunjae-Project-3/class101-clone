package net.fullstack.class101clone.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatDTO {
    private Long idx;
    private String sender;
    private String receiver;
    private String content;
    private String timestamp; // 문자열 형태로 전달
}
