package net.fullstack.class101clone.dto;

import lombok.*;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BbsFilesDTO {
    private String uuid;
    private String fileName;
    private String fileType;
    private long fileSize;
    private int ord;

}
