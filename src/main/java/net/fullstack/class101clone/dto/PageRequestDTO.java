package net.fullstack.class101clone.dto;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@ToString
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageRequestDTO {
    @Builder.Default
    private int pageSkipCount = 0;

    @Builder.Default
    private int pageNo = 1;

    @Builder.Default
    private int pageSize = 10;

    @Builder.Default
    private int pageBlockSize = 10;

    private String search_word;
    private String[] search_categories;
    private String search_category;
    private String search_date_from;
    private String search_date_to;
    private String link_params;
    private String[] sort_fields;
    private String sort_field;
    private String sort_order;

    public String[] getSearch_categories() {
        if (search_categories == null || this.search_category.isEmpty()) {
            return null;
        }
        return search_category.split(",");
    }

    public int getPageNo() {
        return pageNo < 1 ? 1 : pageNo;
    }

    public int getPageSize() {
        return pageSize < 1 ? 10 : pageSize;
    }

//    public int getPageSkipCount() {
//        return Math.max((getPageNo() - 1) * getPageSize(), 0);
//    }
//
//    public Pageable getPageable() {
//        return PageRequest.of(getPageNo() - 1, getPageSize(), Sort.by("idx").descending());
//    }

    public PageRequest getPageable(String...params) {
        return PageRequest.of(getPageNo() - 1, getPageSize(), Sort.by(params).descending());
    }

}
