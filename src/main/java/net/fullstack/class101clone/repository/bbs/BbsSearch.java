package net.fullstack.class101clone.repository.bbs;

import net.fullstack.api.domain.BbsEntity;
import net.fullstack.api.dto.BbsListDTO;
import net.fullstack.api.dto.BbsListWithFileDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BbsSearch {
    Page<BbsEntity> search(Pageable pageable, String[] categories, String search_word);
    // 게시글 목록 : 다중 카테고리 검색, 다중 키워드 검색, 페이징
    Page<BbsListDTO> searchBbsWithReplyCnt(String[] categories, String keyword, Pageable pageable);
    Page<BbsListWithFileDTO> searchBbsWithFiles(String[] categories, String keyword, Pageable pageable);
}