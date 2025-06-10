package net.fullstack.class101clone.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;

@Log4j2
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BbsReplyDTO {
    private long idx;
    @PositiveOrZero
    @NotNull
    private long board_idx;
//    @NotBlank
    private String reply_user_id;
    @NotBlank
    private String reply_content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reply_date;
}
