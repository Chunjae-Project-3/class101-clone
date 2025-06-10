package net.fullstack.class101clone.repository.bbs;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import net.fullstack.api.domain.BbsEntity;
import net.fullstack.api.domain.QBbsEntity;
import net.fullstack.api.domain.QBbsReplyEntity;
import net.fullstack.api.dto.BbsFilesDTO;
import net.fullstack.api.dto.BbsListDTO;
import net.fullstack.api.dto.BbsListWithFileDTO;
import net.fullstack.api.util.CommonDateUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class BbsSearchImpl extends QuerydslRepositorySupport implements BbsSearch {
    public BbsSearchImpl() {
        super(BbsEntity.class);
    }

    public BbsSearchImpl(Class<?> domainClass) {
        super(domainClass);
    }

    @Override
    public Page<BbsEntity> search(Pageable pageable, String[] categories, String search_word) {
        QBbsEntity qBbsEntity = QBbsEntity.bbsEntity;
        JPQLQuery<BbsEntity> query = from(qBbsEntity);

        if ((categories != null && categories.length > 0) && (search_word != null && !search_word.isEmpty())) { // isBlank: " "도 없는 문자 취급
            BooleanBuilder booleanBuilder = new BooleanBuilder(); // 조건에 다 걸리면 안 되니까...
            for (String category : categories) {
                switch (category) {
                    case "title" -> booleanBuilder.or(qBbsEntity.title.likeIgnoreCase(search_word));
                    case "content" -> booleanBuilder.or(qBbsEntity.content.containsIgnoreCase(search_word));
                    case "user_id" ->  // 보안상 안 좋음
                            booleanBuilder.or(qBbsEntity.user_id.containsIgnoreCase(search_word));
                }
            }
            query.where(booleanBuilder);
        }
        //query.where(qBbsEntity.idx.gt(0));
        query.orderBy(qBbsEntity.idx.desc());

        if (this.getQuerydsl() != null) {
            this.getQuerydsl().applyPagination(pageable, query);
        } // 리턴된 결과가 있을 때만 페이지네이션을 처리해라

        List<BbsEntity> list = query.fetch();
        long total_count = query.fetchCount();
        return new PageImpl<>(list, pageable, total_count);
    }

    @Override
    public Page<BbsListDTO> searchBbsWithReplyCnt(String[] categories, String keyword, Pageable pageable) {
        QBbsEntity bbsQ = QBbsEntity.bbsEntity;
        QBbsReplyEntity replyQ = QBbsReplyEntity.bbsReplyEntity;
        JPQLQuery<BbsEntity> bbsListQuery = from(bbsQ);
        bbsListQuery.leftJoin(replyQ).on(replyQ.board.eq(bbsQ)); // 객체로 매핑
        bbsListQuery.groupBy(bbsQ);
        bbsListQuery.where(bbsQ.idx.gt(0));

        if ((categories != null && categories.length > 0) && (keyword != null && !keyword.isEmpty())) {
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            for (String category : categories) {
                switch (category) {
                    case "title" -> booleanBuilder.or(bbsQ.title.likeIgnoreCase(keyword));
                    case "content" -> booleanBuilder.or(bbsQ.content.containsIgnoreCase(keyword));
                    case "user_id" -> booleanBuilder.or(bbsQ.user_id.containsIgnoreCase(keyword));
                    case "reg_date" -> CommonDateUtil.parseLocalDate("yyyy-MM-dd", keyword);
                }
            }
            bbsListQuery.where(booleanBuilder);
        }

        // 빈에 결과 바로 반영해줘
        JPQLQuery<BbsListDTO> dtoQuery = bbsListQuery.select(
                Projections.bean(
                        BbsListDTO.class,
                        bbsQ.idx,
                        bbsQ.user_id,
                        bbsQ.title,
                        bbsQ.content,
                        bbsQ.reg_date,
                        bbsQ.display_date,
                        bbsQ.modify_date,
                        bbsQ.read_cnt,
                        replyQ.count().as("reply_count")
                )
        );

        if (this.getQuerydsl() != null) {
            this.getQuerydsl().applyPagination(pageable, dtoQuery);
        }
        List<BbsListDTO> list = dtoQuery.fetch(); // 결과를 가져올 때는 fetch
        long total_count = dtoQuery.fetchCount();
        PageImpl<BbsListDTO> result = new PageImpl<>(list, pageable, total_count);
        return result;
    }

    @Override
    public Page<BbsListWithFileDTO> searchBbsWithFiles(String[] categories, String keyword, Pageable pageable) {
        QBbsEntity bbsQ = QBbsEntity.bbsEntity;
        QBbsReplyEntity replyQ = QBbsReplyEntity.bbsReplyEntity;
        JPQLQuery<BbsEntity> query = from(bbsQ);
        query.leftJoin(replyQ).on(replyQ.board.eq(bbsQ)); // 객체로 매핑
        query.groupBy(bbsQ);
        query.where(bbsQ.idx.gt(0));

        if ((categories != null && categories.length > 0) && (keyword != null && !keyword.isEmpty())) {
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            for (String category : categories) {
                switch (category) {
                    case "title" -> booleanBuilder.or(bbsQ.title.likeIgnoreCase(keyword));
                    case "content" -> booleanBuilder.or(bbsQ.content.containsIgnoreCase(keyword));
                    case "user_id" -> booleanBuilder.or(bbsQ.user_id.containsIgnoreCase(keyword));
                    case "reg_date" -> CommonDateUtil.parseLocalDate("yyyy-MM-dd", keyword);
                }
            }
            query.where(booleanBuilder);
        }

        if (this.getQuerydsl() != null) {
            this.getQuerydsl().applyPagination(pageable, query);
        }
        JPQLQuery<Tuple> tupleQuery = query.select(bbsQ, replyQ.countDistinct().as("reply_cnt"));
        List<Tuple> tupleList = tupleQuery.fetch();
        long total_count = tupleQuery.fetchCount();
        List<BbsListWithFileDTO> dtoList = tupleList.stream().map(
                tuple -> {
                    BbsEntity bbsEntity = (BbsEntity) tuple.get(bbsQ);
                    long replyCnt = tuple.get(1, Long.class); //replyQ.count().as("reply_cnt")
                    BbsListWithFileDTO dto = BbsListWithFileDTO.builder()
                            .idx(bbsEntity.getIdx())
                            .title(bbsEntity.getTitle())
                            .content(bbsEntity.getContent())
                            .user_id(bbsEntity.getUser_id())
                            .display_date(bbsEntity.getDisplay_date())
//                            .reg_date(bbsEntity.getReg_date())
//                            .bbs_replyCnt(replyCnt)
                            .build();
                    List<BbsFilesDTO> bbsFilesDTOS = bbsEntity.getBbsFileEntitySet().stream()
                            .sorted()
                            .map(
                                    bbsFileEntity -> BbsFilesDTO.builder()
                                            .uuid(bbsFileEntity.getUuid())
                                            .fileName(bbsFileEntity.getFileName())
                                            .fileType(bbsFileEntity.getFileType())
                                            .fileSize(bbsFileEntity.getFileSize())
                                            .build()
                            ).toList();
                    dto.setBbs_files(bbsFilesDTOS);
                    return dto;
                }
        ).toList();

        return new PageImpl<>(dtoList, pageable, total_count);
    }
}