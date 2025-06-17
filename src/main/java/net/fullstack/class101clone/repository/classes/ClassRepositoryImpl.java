package net.fullstack.class101clone.repository.classes;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.fullstack.class101clone.domain.*;
import net.fullstack.class101clone.dto.ClassDTO;
import net.fullstack.class101clone.dto.SubCategoryDTO;
import net.fullstack.class101clone.dto.classes.LectureDTO;
import net.fullstack.class101clone.dto.classes.LectureHistoryDTO;
import net.fullstack.class101clone.dto.classes.LectureHistoryResponseDTO;
import net.fullstack.class101clone.dto.classes.SectionDTO;
import net.fullstack.class101clone.dto.file.FileDTO;
import net.fullstack.class101clone.exception.NotFoundException;
import net.fullstack.class101clone.repository.login.UserRepositoryIf;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@RequiredArgsConstructor
public class ClassRepositoryImpl implements ClassRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final UserRepositoryIf userRepository;
    private final ClassLikeRepository classLikeRepository;

    @Override
    public List<ClassDTO> getClasses(String category, String userId) {
        QClassEntity classQ = QClassEntity.classEntity;
        QFileEntity fileQ = QFileEntity.fileEntity;
        QCategoryEntity categoryQ = QCategoryEntity.categoryEntity;
        QSubCategoryEntity subCategoryQ = QSubCategoryEntity.subCategoryEntity;
        QCreatorEntity creatorQ = QCreatorEntity.creatorEntity;

        List<ClassDTO> classList = queryFactory
                .select(Projections.bean(ClassDTO.class,
                        classQ.classIdx,
                        classQ.classTitle,
                        classQ.classDescription,
                        Expressions.stringTemplate("CONCAT({0}, '/', {1})", fileQ.filePath, fileQ.fileName).as("thumbnailUrl"),
                        categoryQ.categoryIdx,
                        categoryQ.categoryName,
                        subCategoryQ.subCategoryIdx,
                        subCategoryQ.subCategoryName,
                        creatorQ.creatorName,
                        creatorQ.creatorProfileImg,
                        creatorQ.creatorDescription,
                        classQ.createdAt
                ))
                .from(classQ)
                .leftJoin(classQ.classThumbnailImg, fileQ)
                .leftJoin(classQ.classCategory, categoryQ)
                .leftJoin(classQ.classSubCategory, subCategoryQ)
                .leftJoin(classQ.creator, creatorQ)
                .where(category != null ? categoryQ.categoryName.eq(category) : null)
                .orderBy(classQ.createdAt.desc())
                .fetch();

        UserEntity user = userRepository.findByUserId(userId).orElse(null);
        if (user != null) {
            classList.forEach(dto -> {
                boolean liked = classLikeRepository.existsByClassLikeUser_UserIdAndClassLikeRef_ClassIdx(userId, dto.getClassIdx());
                dto.setLiked(liked);
            });
        }
        return classList;
    }

    @Override
    public List<ClassDTO> getTopLikedClasses(int limit) {
        QClassEntity classQ = QClassEntity.classEntity;
        QClassLikeEntity classLikeQ = QClassLikeEntity.classLikeEntity;
        QFileEntity fileQ = QFileEntity.fileEntity;
        QCategoryEntity categoryQ = QCategoryEntity.categoryEntity;
        QSubCategoryEntity subCategoryQ = QSubCategoryEntity.subCategoryEntity;
        QCreatorEntity creatorQ = QCreatorEntity.creatorEntity;

        return queryFactory
                .select(Projections.bean(ClassDTO.class,
                        classQ.classIdx,
                        classQ.classTitle,
                        classQ.classDescription,
                        Expressions.stringTemplate("CONCAT({0}, '/', {1})", fileQ.filePath, fileQ.fileName).as("thumbnailUrl"),
                        categoryQ.categoryIdx,
                        categoryQ.categoryName,
                        subCategoryQ.subCategoryIdx,
                        subCategoryQ.subCategoryName,
                        creatorQ.creatorName,
                        creatorQ.creatorProfileImg,
                        creatorQ.creatorDescription,
                        classQ.createdAt
                ))
                .from(classQ)
                .leftJoin(classQ.classThumbnailImg, fileQ)
                .leftJoin(classQ.classCategory, categoryQ)
                .leftJoin(classQ.classSubCategory, subCategoryQ)
                .leftJoin(classQ.creator, creatorQ)
                .leftJoin(classLikeQ).on(classLikeQ.classLikeRef.eq(classQ))
                .groupBy(classQ.classIdx)
                .orderBy(classLikeQ.count().desc())
                .limit(limit)
                .fetch();
    }

    @Override
    public List<ClassDTO> getRecentClasses(int limit) {
        QClassEntity classQ = QClassEntity.classEntity;
        QFileEntity fileQ = QFileEntity.fileEntity;
        QCategoryEntity categoryQ = QCategoryEntity.categoryEntity;
        QSubCategoryEntity subCategoryQ = QSubCategoryEntity.subCategoryEntity;
        QCreatorEntity creatorQ = QCreatorEntity.creatorEntity;

        return queryFactory
                .select(Projections.bean(ClassDTO.class,
                        classQ.classIdx,
                        classQ.classTitle,
                        classQ.classDescription,
                        Expressions.stringTemplate("CONCAT({0}, '/', {1})", fileQ.filePath, fileQ.fileName).as("thumbnailUrl"),
                        categoryQ.categoryIdx,
                        categoryQ.categoryName,
                        subCategoryQ.subCategoryIdx,
                        subCategoryQ.subCategoryName,
                        creatorQ.creatorName,
                        creatorQ.creatorProfileImg,
                        creatorQ.creatorDescription,
                        classQ.createdAt
                ))
                .from(classQ)
                .leftJoin(classQ.classThumbnailImg, fileQ)
                .leftJoin(classQ.classCategory, categoryQ)
                .leftJoin(classQ.classSubCategory, subCategoryQ)
                .leftJoin(classQ.creator, creatorQ)
                .orderBy(classQ.createdAt.desc())
                .limit(limit)
                .fetch();
    }

    @Override
    public List<ClassDTO> getClassesByCategoryIdx(Integer categoryIdx) {
        QClassEntity classQ = QClassEntity.classEntity;
        QFileEntity fileQ = QFileEntity.fileEntity;
        QCategoryEntity categoryQ = QCategoryEntity.categoryEntity;
        QSubCategoryEntity subCategoryQ = QSubCategoryEntity.subCategoryEntity;
        QCreatorEntity creatorQ = QCreatorEntity.creatorEntity;

        return queryFactory
                .select(Projections.bean(ClassDTO.class,
                        classQ.classIdx,
                        classQ.classTitle,
                        classQ.classDescription,
                        Expressions.stringTemplate("CONCAT({0}, '/', {1})", fileQ.filePath, fileQ.fileName).as("thumbnailUrl"),
                        categoryQ.categoryIdx,
                        categoryQ.categoryName,
                        subCategoryQ.subCategoryIdx,
                        subCategoryQ.subCategoryName,
                        creatorQ.creatorName,
                        creatorQ.creatorProfileImg,
                        creatorQ.creatorDescription,
                        classQ.createdAt
                ))
                .from(classQ)
                .leftJoin(classQ.classThumbnailImg, fileQ)
                .leftJoin(classQ.classCategory, categoryQ)
                .leftJoin(classQ.classSubCategory, subCategoryQ)
                .leftJoin(classQ.creator, creatorQ)
                .where(categoryQ.categoryIdx.eq(categoryIdx))
                .orderBy(classQ.createdAt.desc())
                .fetch();
    }

    @Override
    public Page<ClassDTO> getClassesByCategoryIdx(Integer categoryIdx, Pageable pageable, String sort) {
        QClassEntity classQ = QClassEntity.classEntity;
        QFileEntity fileQ = QFileEntity.fileEntity;
        QCategoryEntity categoryQ = QCategoryEntity.categoryEntity;
        QSubCategoryEntity subCategoryQ = QSubCategoryEntity.subCategoryEntity;
        QCreatorEntity creatorQ = QCreatorEntity.creatorEntity;

        var query = queryFactory
                .select(Projections.bean(ClassDTO.class,
                        classQ.classIdx,
                        classQ.classTitle,
                        classQ.classDescription,
                        Expressions.stringTemplate("CONCAT({0}, '/', {1})", fileQ.filePath, fileQ.fileName).as("thumbnailUrl"),
                        categoryQ.categoryIdx,
                        categoryQ.categoryName,
                        subCategoryQ.subCategoryIdx,
                        subCategoryQ.subCategoryName,
                        creatorQ.creatorName,
                        creatorQ.creatorProfileImg,
                        creatorQ.creatorDescription,
                        classQ.createdAt
                ))
                .from(classQ)
                .leftJoin(classQ.classThumbnailImg, fileQ)
                .leftJoin(classQ.classCategory, categoryQ)
                .leftJoin(classQ.classSubCategory, subCategoryQ)
                .leftJoin(classQ.creator, creatorQ)
                .where(categoryQ.categoryIdx.eq(categoryIdx));

        switch (sort) {
           case "popular":
               QClassLikeEntity classLikeQ = QClassLikeEntity.classLikeEntity;
               query
                       .leftJoin(classLikeQ).on(classLikeQ.classLikeRef.eq(classQ))
                       .groupBy(classQ.classIdx)
                       .orderBy(classLikeQ.count().desc());
                break;
            case "old":
                query.orderBy(classQ.createdAt.asc());
                break;
            default:
                query.orderBy(classQ.createdAt.desc());
        }

        List<ClassDTO> result = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(classQ.count())
                .from(classQ)
                .leftJoin(classQ.classCategory, categoryQ)
                .where(categoryQ.categoryIdx.eq(categoryIdx))
                .fetchOne();

        return new PageImpl<>(result, pageable, total);
    }

    @Override
    public List<CreatorEntity> getCreatorsByCategoryIdx(Integer categoryIdx) {
        QClassEntity classQ = QClassEntity.classEntity;
        QCreatorEntity creatorQ = QCreatorEntity.creatorEntity;

        return queryFactory
                .select(creatorQ)
                .from(classQ)
                .join(classQ.creator, creatorQ)
                .where(classQ.classCategory.categoryIdx.eq(categoryIdx))
                .distinct()
                .fetch();
    }

    @Override
    public ClassDTO getClassByIdx(int classIdx) {
        QClassEntity classQ = QClassEntity.classEntity;
        QFileEntity fileQ = QFileEntity.fileEntity;
        QCategoryEntity categoryQ = QCategoryEntity.categoryEntity;
        QSubCategoryEntity subCategoryQ = QSubCategoryEntity.subCategoryEntity;
        QCreatorEntity creatorQ = QCreatorEntity.creatorEntity;

        ClassDTO dto = queryFactory
                .select(Projections.bean(ClassDTO.class,
                        classQ.classIdx,
                        classQ.classTitle,
                        classQ.classDescription,
                        Expressions.stringTemplate("CONCAT({0}, '/', {1})", fileQ.filePath, fileQ.fileName).as("thumbnailUrl"),
                        categoryQ.categoryIdx,
                        categoryQ.categoryName,
                        subCategoryQ.subCategoryIdx,
                        subCategoryQ.subCategoryName,
                        creatorQ.creatorId,
                        creatorQ.creatorName,
                        creatorQ.creatorProfileImg,
                        creatorQ.creatorDescription,
                        creatorQ.createdAt
                ))
                .from(classQ)
                .leftJoin(classQ.classThumbnailImg, fileQ)
                .leftJoin(classQ.classCategory, categoryQ)
                .leftJoin(classQ.classSubCategory, subCategoryQ)
                .leftJoin(classQ.creator, creatorQ)
                .where(classQ.classIdx.eq(classIdx))
                .fetchOne();

        if (dto == null) throw new NotFoundException("Class not found. id: " + classIdx);

        return dto;
    }

    @Override
    public Map<String, Object> searchClassesAndCreators(String keyword, Pageable pageable, String sort, String userId) {
        QClassEntity classQ = QClassEntity.classEntity;
        QFileEntity fileQ = QFileEntity.fileEntity;
        QCategoryEntity categoryQ = QCategoryEntity.categoryEntity;
        QClassLikeEntity classLikeQ = QClassLikeEntity.classLikeEntity;
        QCreatorEntity creatorQ = QCreatorEntity.creatorEntity;

        var query = queryFactory
                .select(Projections.bean(ClassDTO.class,
                        classQ.classIdx,
                        classQ.classTitle,
                        classQ.classDescription,
                        Expressions.stringTemplate("CONCAT({0}, '/', {1})", fileQ.filePath, fileQ.fileName).as("thumbnailUrl"),
                        categoryQ.categoryIdx,
                        categoryQ.categoryName,
                        creatorQ.creatorId,
                        creatorQ.creatorName,
                        creatorQ.creatorProfileImg,
                        creatorQ.creatorDescription
                ))
                .from(classQ)
                .leftJoin(classQ.classThumbnailImg, fileQ)
                .leftJoin(classQ.classCategory, categoryQ)
                .leftJoin(classQ.creator, creatorQ)
                .where(
                        classQ.classTitle.containsIgnoreCase(keyword)
                                .or(classQ.classDescription.isNotNull().and(classQ.classDescription.containsIgnoreCase(keyword)))
                                .or(creatorQ.creatorName.containsIgnoreCase(keyword))
                                .or(creatorQ.creatorDescription.isNotNull().and(creatorQ.creatorDescription.containsIgnoreCase(keyword)))

                );

        // 정렬 조건 분기
        switch (sort) {
            case "popular":
                query
                        .leftJoin(classLikeQ).on(classLikeQ.classLikeRef.eq(classQ))
                        .groupBy(classQ.classIdx)
                        .orderBy(classLikeQ.count().desc());
                break;
            case "old":
                query.orderBy(classQ.createdAt.asc());
                break;
            default:
                query.orderBy(classQ.createdAt.desc());
        }

        // 페이징 + 결과 조회
        List<ClassDTO> classResults = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 찜 여부 처리
        if (userId != null) {
            UserEntity user = userRepository.findByUserId(userId).orElse(null);
            if (user != null) {
                for (ClassDTO dto : classResults) {
                    boolean liked = classLikeRepository.existsByClassLikeUser_UserIdAndClassLikeRef_ClassIdx(userId, dto.getClassIdx());
                    dto.setLiked(liked);
                }
            }
        }

        // 전체 개수 (정렬/페이징과 무관하게 동일한 조건으로)
        long total = queryFactory
                .select(classQ.count())
                .from(classQ)
                .leftJoin(classQ.creator, creatorQ)
                .where(
                        classQ.classTitle.containsIgnoreCase(keyword)
                                .or(classQ.classDescription.containsIgnoreCase(keyword))
                                .or(creatorQ.creatorName.containsIgnoreCase(keyword))
                                .or(creatorQ.creatorDescription.containsIgnoreCase(keyword))
                )
                .fetchOne();

        // 크리에이터 결과
        List<CreatorEntity> creatorResults = queryFactory
                .select(creatorQ)
                .from(classQ)
                .leftJoin(classQ.creator, creatorQ)
                .where(
                        creatorQ.creatorName.containsIgnoreCase(keyword)
                                .or(creatorQ.creatorDescription.containsIgnoreCase(keyword))
                )
                .distinct()
                .fetch();

        return Map.of(
                "classes", new PageImpl<>(classResults, pageable, total),
                "creators", creatorResults
        );
    }

    @Override
    public Page<ClassDTO> getPagedClassesByCategoryAndSub(Integer categoryIdx, Integer subCategoryIdx, Pageable pageable, String sort) {
        QClassEntity classQ = QClassEntity.classEntity;
        QFileEntity fileQ = QFileEntity.fileEntity;
        QCategoryEntity categoryQ = QCategoryEntity.categoryEntity;
        QSubCategoryEntity subCategoryQ = QSubCategoryEntity.subCategoryEntity;
        QCreatorEntity creatorQ = QCreatorEntity.creatorEntity;
        QClassLikeEntity classLikeQ = QClassLikeEntity.classLikeEntity;

        var query = queryFactory
                .select(Projections.bean(ClassDTO.class,
                        classQ.classIdx,
                        classQ.classTitle,
                        classQ.classDescription,
                        Expressions.stringTemplate("CONCAT({0}, '/', {1})", fileQ.filePath, fileQ.fileName).as("thumbnailUrl"),
                        categoryQ.categoryIdx,
                        categoryQ.categoryName,
                        subCategoryQ.subCategoryIdx,
                        subCategoryQ.subCategoryName,
                        creatorQ.creatorId,
                        creatorQ.creatorName,
                        creatorQ.creatorProfileImg,
                        creatorQ.creatorDescription,
                        classQ.createdAt
                ))
                .from(classQ)
                .leftJoin(classQ.classThumbnailImg, fileQ)
                .leftJoin(classQ.classCategory, categoryQ)
                .leftJoin(classQ.classSubCategory, subCategoryQ)
                .leftJoin(classQ.creator, creatorQ)
                .where(
                        categoryQ.categoryIdx.eq(categoryIdx),
                        subCategoryIdx != null ? subCategoryQ.subCategoryIdx.eq(subCategoryIdx) : null
                );

        switch (sort) {
            case "popular" -> {
                query
                        .leftJoin(classLikeQ).on(classLikeQ.classLikeRef.eq(classQ))
                        .groupBy(classQ.classIdx)
                        .orderBy(classLikeQ.count().desc());
            }
            case "old" -> query.orderBy(classQ.createdAt.asc());
            default -> query.orderBy(classQ.createdAt.desc());
        }

        List<ClassDTO> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(classQ.count())
                .from(classQ)
                .leftJoin(classQ.classCategory, categoryQ)
                .leftJoin(classQ.classSubCategory, subCategoryQ)
                .where(
                        categoryQ.categoryIdx.eq(categoryIdx),
                        subCategoryIdx != null ? subCategoryQ.subCategoryIdx.eq(subCategoryIdx) : null
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }

    @Override
    public List<ClassDTO> getWishListByUserId(String userId) {
        if (userId == null) {
            return Collections.emptyList();
        }

        QClassEntity classQ = QClassEntity.classEntity;
        QFileEntity fileQ = QFileEntity.fileEntity;
        QCategoryEntity categoryQ = QCategoryEntity.categoryEntity;
        QCreatorEntity creatorQ = QCreatorEntity.creatorEntity;
        QClassLikeEntity classLikeQ = QClassLikeEntity.classLikeEntity;

        List<ClassDTO> classList = queryFactory
                .select(Projections.bean(ClassDTO.class,
                        classQ.classIdx,
                        classQ.classTitle,
                        classQ.classDescription,
                        Expressions.stringTemplate("CONCAT({0}, '/', {1})", fileQ.filePath, fileQ.fileName).as("thumbnailUrl"),
                        categoryQ.categoryIdx,
                        categoryQ.categoryName,
                        creatorQ.creatorId,
                        creatorQ.creatorName,
                        creatorQ.creatorProfileImg,
                        creatorQ.creatorDescription
                ))
                .from(classQ)
                .leftJoin(classQ.classThumbnailImg, fileQ)
                .leftJoin(classQ.classCategory, categoryQ)
                .leftJoin(classQ.creator, creatorQ)
                .innerJoin(classLikeQ).on(classLikeQ.classLikeRef.eq(classQ))  // leftJoin -> innerJoin
                .where(classLikeQ.classLikeUser.userId.eq(userId))
                .orderBy(classQ.createdAt.desc())
                .fetch();

        classList.forEach(dto -> dto.setLiked(true));

        return classList;
    }

    @Override
    public List<SubCategoryDTO> getSubCategoriesByCategory(Integer categoryIdx) {
        QSubCategoryEntity sub = QSubCategoryEntity.subCategoryEntity;

        return queryFactory
                .select(Projections.bean(SubCategoryDTO.class,
                        sub.subCategoryIdx,
                        sub.subCategoryName
                ))
                .from(sub)
                .where(sub.parentCategory.categoryIdx.eq(categoryIdx))
                .orderBy(sub.subCategoryIdx.asc()) // 순서 정렬
                .fetch();
    }

    @Override
    public List<SectionDTO> findSectionsByClassIdx(Integer classIdx) {
        QSectionEntity sectionQ = QSectionEntity.sectionEntity;

        return queryFactory
                .select(Projections.bean(SectionDTO.class,
                        sectionQ.sectionIdx,
                        sectionQ.sectionRef.classIdx.as("sectionRefIdx"),
                        sectionQ.sectionTitle,
                        sectionQ.sectionOrder
                ))
                .from(sectionQ)
                .where(sectionQ.sectionRef.classIdx.eq(classIdx))
                .orderBy(sectionQ.sectionOrder.asc())
                .fetch();
    }

    @Override
    public List<SectionDTO> getSectionsWithFilesByClassIdx(Integer classIdx) {
        QSectionEntity sectionQ = QSectionEntity.sectionEntity;
        QSectionFileEntity sectionFileQ = QSectionFileEntity.sectionFileEntity;
        QFileEntity fileQ = QFileEntity.fileEntity;

        List<Tuple> tuples = queryFactory
                .select(sectionQ, fileQ)
                .from(sectionQ)
                .leftJoin(sectionQ.sectionFileList, sectionFileQ)
                .leftJoin(sectionFileQ.file, fileQ)
                .where(sectionQ.sectionRef.classIdx.eq(classIdx))
                .orderBy(sectionQ.sectionOrder.asc(), sectionFileQ.ord.asc())
                .fetch();

        if (tuples.isEmpty()) {
            throw new NotFoundException("Sections not found. id: " + classIdx);
        }

        Map<Integer, SectionDTO> sectionMap = new LinkedHashMap<>();

        for (Tuple tuple : tuples) {
            SectionEntity sectionEntity = tuple.get(sectionQ);
            FileEntity fileEntity = tuple.get(fileQ);

            Integer sectionIdx = sectionEntity.getSectionIdx();

            // 이미 만들어진 sectionDTO 가 있는지
            SectionDTO sectionDTO = sectionMap.get(sectionIdx);

            // 없을 경우, sectionIdx 로 DTO 생성
            if (sectionDTO == null) {
                sectionDTO = SectionDTO.builder()
                        .sectionIdx(sectionEntity.getSectionIdx())
                        .sectionRefIdx(sectionEntity.getSectionRef().getClassIdx())
                        .sectionTitle(sectionEntity.getSectionTitle())
                        .sectionOrder(sectionEntity.getSectionOrder())
                        .sectionThumbnailUrls(new ArrayList<>())
                        .build();
                sectionMap.put(sectionIdx, sectionDTO);
            }

            // 파일이 있으면 DTO 안에 추가
            if (fileEntity != null) {
                String thumbnailUrl = fileEntity.getFilePath() + "/" + fileEntity.getFileName();
                sectionDTO.getSectionThumbnailUrls().add(thumbnailUrl);
            }
        }

        // Map -> List 로 변환 후 반환
        return new ArrayList<>(sectionMap.values());
    }

    @Override
    public List<FileDTO> findFilesByClassIdx(Integer classIdx) {
        QSectionEntity sectionQ = QSectionEntity.sectionEntity;
        QSectionFileEntity sectionFileQ = QSectionFileEntity.sectionFileEntity;
        QFileEntity fileQ = QFileEntity.fileEntity;

        return queryFactory
                .select(Projections.bean(FileDTO.class,
                        fileQ.fileIdx,
                        fileQ.fileName,
                        fileQ.fileOrgName,
                        fileQ.fileExt,
                        fileQ.fileSize,
                        fileQ.filePath
                ))
                .from(sectionFileQ)
                .innerJoin(sectionFileQ.section, sectionQ)
                .innerJoin(sectionFileQ.file, fileQ)
                .where(sectionQ.sectionRef.classIdx.eq(classIdx))
                .orderBy(sectionQ.sectionOrder.asc(), sectionFileQ.ord.asc())
                .fetch();
    }

    @Override
    public List<LectureDTO> findLecturesBySectionIdx(Integer sectionIdx) {
        QLectureEntity lectureQ = QLectureEntity.lectureEntity;
        QFileEntity fileQ = QFileEntity.fileEntity;

        return queryFactory
                .select(Projections.bean(LectureDTO.class,
                        lectureQ.lectureIdx,
                        lectureQ.lectureRef.sectionIdx.as("lectureRefIdx"),
                        lectureQ.lectureTitle,
                        lectureQ.lectureVideo.fileIdx.as("lectureVideoIdx"),
                        lectureQ.lectureDuration,
                        lectureQ.lectureOrder
                ))
                .from(lectureQ)
                .innerJoin(lectureQ.lectureVideo, fileQ)
                .where(lectureQ.lectureRef.sectionIdx.eq(sectionIdx))
                .orderBy(lectureQ.lectureOrder.asc())
                .fetch();
    }

    @Override
    public List<LectureDTO> findLecturesBySectionIdx(String userId, Integer sectionIdx) {
        QLectureEntity lectureQ = QLectureEntity.lectureEntity;
        QLectureHistoryEntity historyQ = QLectureHistoryEntity.lectureHistoryEntity;
        QFileEntity fileQ = QFileEntity.fileEntity;

        return queryFactory
                .select(Projections.bean(LectureDTO.class,
                        lectureQ.lectureIdx,
                        lectureQ.lectureRef.sectionIdx.as("lectureRefIdx"),
                        lectureQ.lectureTitle,
                        lectureQ.lectureVideo.fileIdx.as("lectureVideoIdx"),
                        lectureQ.lectureDuration,
                        lectureQ.lectureOrder,
                        Projections.bean(LectureHistoryDTO.class,
                                historyQ.lectureHistoryLastPosition.as("lastPosition"),
                                historyQ.lectureHistoryTotalWatchTime.as("totalWatchTime"),
                                historyQ.lectureHistoryLastWatchDate.as("lastWatchDate")
                        ).as("lectureHistory")
                ))
                .from(lectureQ)
                .innerJoin(lectureQ.lectureVideo, fileQ)
                .leftJoin(historyQ).on(
                        lectureQ.lectureIdx.eq(historyQ.lectureHistoryRef.lectureIdx)
                                .and(historyQ.lectureHistoryUser.userId.eq(userId))
                )
                .where(lectureQ.lectureRef.sectionIdx.eq(sectionIdx))
                .orderBy(lectureQ.lectureOrder.asc())
                .fetch();
    }

    @Override
    public List<LectureHistoryResponseDTO> findLectureHistory(String userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        QClassEntity classQ = QClassEntity.classEntity;
        QLectureEntity lectureQ = QLectureEntity.lectureEntity;
        QLectureHistoryEntity historyQ = QLectureHistoryEntity.lectureHistoryEntity;

        return queryFactory
                .select(Projections.bean(LectureHistoryResponseDTO.class,
                        classQ.classIdx,
                        classQ.classTitle,
                        lectureQ.lectureIdx,
                        lectureQ.lectureTitle,
                        Expressions.stringTemplate("CONCAT({0}, '/', {1})", classQ.classThumbnailImg.filePath, classQ.classThumbnailImg.fileName).as("thumbnailUrl"),
                        historyQ.lectureHistoryLastPosition.as("lastPosition"),
                        historyQ.lectureHistoryLastWatchDate.as("lastWatchDate"),
                        historyQ.lectureHistoryTotalWatchTime.as("totalWatchTime"),
                        (historyQ.lectureHistoryLastPosition.multiply(100).doubleValue().divide(lectureQ.lectureDuration)).as("progress")
                ))
                .from(historyQ)
                .innerJoin(historyQ.lectureHistoryRef, lectureQ)
                .innerJoin(lectureQ.lectureRef.sectionRef, classQ)
                .where(historyQ.lectureHistoryUser.userId.eq(userId))
                .orderBy(historyQ.lectureHistoryLastWatchDate.desc())
                .fetch();
    }
}
