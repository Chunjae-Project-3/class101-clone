package net.fullstack.class101clone.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
@Data
public class PageResponseDTO<E> {
    private int totalCount;
    private int pageNo;
    private int pageSize;
    private int totalPage;
    private int pageSkipCount;
    private int pageBlockSize;
    private int pageBlockStart;
    private int pageBlockEnd;

    private int firstPage;
    private int lastPage;
    private boolean prevPageFlag;
    private boolean nextPageFlag;

    private List<E> dtoList;

    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(PageRequestDTO pageRequestDTO, int totalCount, List<E> dtoList) {
        this.totalCount = Math.max(0, totalCount);

        this.pageSize = pageRequestDTO.getPageSize() < 1 ? 10 : pageRequestDTO.getPageSize();
        this.totalPage = this.totalCount > 0 ? (int) Math.ceil((double) this.totalCount / this.pageSize) : 1;

        int inputPageNo = pageRequestDTO.getPageNo();
        this.pageNo = inputPageNo < 1 ? 1 : Math.min(inputPageNo, this.totalPage);
        this.pageSkipCount = Math.max((this.pageNo - 1) * this.pageSize, 0);

        this.pageBlockSize = pageRequestDTO.getPageBlockSize() < 1 ? 10 : pageRequestDTO.getPageBlockSize();
        this.pageBlockStart = ((this.pageNo - 1) / this.pageBlockSize) * this.pageBlockSize + 1;
        this.pageBlockEnd = Math.min(this.pageBlockStart + this.pageBlockSize - 1, this.totalPage);

        this.prevPageFlag = this.pageBlockStart > 1;
        this.nextPageFlag = this.pageBlockEnd < totalPage;

        this.firstPage = 1;
        this.lastPage = this.totalPage;

        this.dtoList = dtoList;
    }

    public int getTotalPage() {
        return Math.max(1, (int) Math.ceil((double) this.totalCount / Math.max(this.pageSize, 1)));
    }

    public int getPageSkipCount() {
        return Math.max((Math.max(this.pageNo, 1) - 1) * Math.max(this.pageSize, 1), 0);
    }

}
