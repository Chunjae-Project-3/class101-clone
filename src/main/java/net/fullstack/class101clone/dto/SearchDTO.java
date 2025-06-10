package net.fullstack.class101clone.dto;

import lombok.Data;


@Data
public class SearchDTO {
    private String searchWord;
    private String searchType;
    private String dateType;
    private String startDate;
    private String endDate;
    private String sortColumn;
    private String sortOrder;
}
