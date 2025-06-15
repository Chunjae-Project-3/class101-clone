package net.fullstack.class101clone.repository.classes;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.fullstack.class101clone.domain.*;
import net.fullstack.class101clone.dto.ClassDTO;
import net.fullstack.class101clone.dto.LectureDTO;
import net.fullstack.class101clone.repository.login.UserRepositoryIf;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ClassRepositoryImpl implements ClassRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final UserRepositoryIf userRepository;
    private final ClassLikeRepository classLikeRepository;

    @Override
    public List<ClassDTO> getClasses(String category, String userId) {
        QClassEntity cls = QClassEntity.classEntity;
        QFileEntity file = QFileEntity.fileEntity;
        QCategoryEntity cat = QCategoryEntity.categoryEntity;
        QCreatorEntity creator = QCreatorEntity.creatorEntity;

        List<ClassDTO> classList = queryFactory
                .select(Projections.constructor(ClassDTO.class,
                        cls.classIdx,
                        cls.classTitle,
                        cls.classDescription,
                        file.filePath,
                        cat.categoryName,
                        creator.creatorName,
                        creator.creatorProfileImg,
                        creator.creatorDescription
                ))
                .from(cls)
                .leftJoin(cls.classThumbnailImg, file)
                .leftJoin(cls.classCategory, cat)
                .leftJoin(cls.creator, creator)
                .where(category != null ? cat.categoryName.eq(category) : null)
                .orderBy(cls.createdAt.desc())
                .fetch();

        if (userId == null) {
            classList.forEach(dto -> dto.setLiked(false));
            return classList;
        }

        UserEntity user = userRepository.findByUserId(userId).orElse(null);
        if (user == null) {
            classList.forEach(dto -> dto.setLiked(false));
            return classList;
        }

        for (ClassDTO dto : classList) {
            boolean liked = classLikeRepository.existsByClassLikeUser_UserIdAndClassLikeRef_ClassIdx(userId, dto.getClassIdx());
            dto.setLiked(liked);
        }

        return classList;
    }
    @Override
    public List<ClassDTO> getTopLikedClasses(int limit) {
        QClassEntity cls = QClassEntity.classEntity;
        QClassLikeEntity like = QClassLikeEntity.classLikeEntity;
        QFileEntity file = QFileEntity.fileEntity;
        QCategoryEntity cat = QCategoryEntity.categoryEntity;
        QCreatorEntity creator = QCreatorEntity.creatorEntity;

        return queryFactory
                .select(Projections.constructor(ClassDTO.class,
                        cls.classIdx,
                        cls.classTitle,
                        cls.classDescription,
                        file.filePath,
                        cat.categoryName,
                        creator.creatorName,
                        creator.creatorProfileImg,
                        creator.creatorDescription
                ))
                .from(cls)
                .leftJoin(cls.classThumbnailImg, file)
                .leftJoin(cls.classCategory, cat)
                .leftJoin(cls.creator, creator)
                .leftJoin(like).on(like.classLikeRef.eq(cls))
                .groupBy(cls.classIdx)
                .orderBy(like.count().desc())
                .limit(limit)
                .fetch();
    }

    @Override
    public List<ClassDTO> getRecentClasses(int limit) {
        QClassEntity cls = QClassEntity.classEntity;
        QFileEntity file = QFileEntity.fileEntity;
        QCategoryEntity cat = QCategoryEntity.categoryEntity;
        QCreatorEntity creator = QCreatorEntity.creatorEntity;

        return queryFactory
                .select(Projections.constructor(ClassDTO.class,
                        cls.classIdx,
                        cls.classTitle,
                        cls.classDescription,
                        file.filePath,
                        cat.categoryName,
                        creator.creatorName,
                        creator.creatorProfileImg,
                        creator.creatorDescription
                ))
                .from(cls)
                .leftJoin(cls.classThumbnailImg, file)
                .leftJoin(cls.classCategory, cat)
                .leftJoin(cls.creator, creator)
                .orderBy(cls.createdAt.desc())
                .limit(limit)
                .fetch();
    }

    @Override
    public List<ClassDTO> getClassesByCategoryIdx(Integer categoryIdx) {
        QClassEntity cls = QClassEntity.classEntity;
        QFileEntity file = QFileEntity.fileEntity;
        QCategoryEntity cat = QCategoryEntity.categoryEntity;
        QCreatorEntity creator = QCreatorEntity.creatorEntity;

        return queryFactory
                .select(Projections.constructor(ClassDTO.class,
                        cls.classIdx,
                        cls.classTitle,
                        cls.classDescription,
                        file.filePath,
                        cat.categoryName,
                        creator.creatorName,
                        creator.creatorProfileImg,
                        creator.creatorDescription
                ))
                .from(cls)
                .leftJoin(cls.classThumbnailImg, file)
                .leftJoin(cls.classCategory, cat)
                .leftJoin(cls.creator, creator)
                .where(cat.categoryIdx.eq(categoryIdx))
                .orderBy(cls.createdAt.desc())
                .fetch();
    }

    @Override
    public Page<ClassDTO> getPagedClassesByCategoryIdx(Integer categoryIdx, Pageable pageable, String sort) {
        QClassEntity cls = QClassEntity.classEntity;
        QFileEntity file = QFileEntity.fileEntity;
        QCategoryEntity cat = QCategoryEntity.categoryEntity;
        QCreatorEntity creator = QCreatorEntity.creatorEntity;
        QClassLikeEntity like = QClassLikeEntity.classLikeEntity;

        var query = queryFactory
                .select(Projections.constructor(ClassDTO.class,
                        cls.classIdx,
                        cls.classTitle,
                        cls.classDescription,
                        file.filePath,
                        cat.categoryName,
                        creator.creatorName,
                        creator.creatorProfileImg,
                        creator.creatorDescription
                ))
                .from(cls)
                .leftJoin(cls.classThumbnailImg, file)
                .leftJoin(cls.classCategory, cat)
                .leftJoin(cls.creator, creator)
                .where(cat.categoryIdx.eq(categoryIdx));

        // 정렬 조건 분기
        switch (sort) {
            case "popular":
                query
                        .leftJoin(like).on(like.classLikeRef.eq(cls))
                        .groupBy(cls.classIdx)
                        .orderBy(like.count().desc());
                break;
            case "old":
                query.orderBy(cls.createdAt.asc());
                break;
            default:
                query.orderBy(cls.createdAt.desc());
        }

        List<ClassDTO> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(cls.count())
                .from(cls)
                .leftJoin(cls.classCategory, cat)
                .where(cat.categoryIdx.eq(categoryIdx))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public List<Map<String, String>> getCreatorListByCategoryIdx(Integer categoryIdx) {
        QClassEntity cls = QClassEntity.classEntity;
        QCreatorEntity creator = QCreatorEntity.creatorEntity;

        return queryFactory
                .select(creator.creatorName, creator.creatorProfileImg, creator.creatorDescription)
                .from(cls)
                .leftJoin(cls.creator, creator)
                .where(cls.classCategory.categoryIdx.eq(categoryIdx))
                .distinct()
                .fetch()
                .stream()
                .map(t -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("name", t.get(creator.creatorName));
                    map.put("profileImage", t.get(creator.creatorProfileImg));
                    map.put("description", t.get(creator.creatorDescription));
                    return map;
                })
                .collect(Collectors.toList());
    }

//    @Override
//    public ClassDTO getClassDetailById(Integer classId) {
//        QClassEntity cls = QClassEntity.classEntity;
//        QFileEntity file = QFileEntity.fileEntity;
//        QCategoryEntity cat = QCategoryEntity.categoryEntity;
//
//        return queryFactory
//                .select(Projections.constructor(ClassDTO.class,
//                        cls.classIdx,
//                        cls.classTitle,
//                        cls.classDescription,
//                        file.filePath,
//                        cat.categoryName
//                ))
//                .from(cls)
//                .leftJoin(cls.classThumbnailImg, file)
//                .leftJoin(cls.classCategory, cat)
//                .where(cls.classIdx.eq(classId))
//                .fetchOne();
//    }

    @Override
    public ClassDTO getClassDetailById(Integer classId) {
        QClassEntity cls = QClassEntity.classEntity;
        QFileEntity file = QFileEntity.fileEntity;
        QCategoryEntity cat = QCategoryEntity.categoryEntity;
        QSubCategoryEntity sub = QSubCategoryEntity.subCategoryEntity;

        return queryFactory
                .select(Projections.constructor(ClassDTO.class,
                        cls.classIdx,
                        cls.classTitle,
                        cls.classDescription,
                        file.filePath,
                        cat.categoryIdx,
                        cat.categoryName,
                        sub.subCategoryIdx,
                        sub.subCategoryName
                ))
                .from(cls)
                .leftJoin(cls.classThumbnailImg, file)
                .leftJoin(cls.classCategory, cat)
                .leftJoin(cls.classSubCategory, sub)
                .where(cls.classIdx.eq(classId))
                .fetchOne();
    }

    @Override
    public Map<String, Object> searchClassesAndCreators(String keyword, Pageable pageable, String sort, String userId) {
        QClassEntity cls = QClassEntity.classEntity;
        QCreatorEntity creator = QCreatorEntity.creatorEntity;
        QFileEntity file = QFileEntity.fileEntity;
        QCategoryEntity cat = QCategoryEntity.categoryEntity;
        QClassLikeEntity like = QClassLikeEntity.classLikeEntity;

        var query = queryFactory
                .select(Projections.constructor(ClassDTO.class,
                        cls.classIdx,
                        cls.classTitle,
                        cls.classDescription,
                        file.filePath,
                        cat.categoryName,
                        creator.creatorName,
                        creator.creatorProfileImg,
                        creator.creatorDescription
                ))
                .from(cls)
                .leftJoin(cls.classThumbnailImg, file)
                .leftJoin(cls.classCategory, cat)
                .leftJoin(cls.creator, creator)
                .where(
                        cls.classTitle.containsIgnoreCase(keyword)
                                .or(cls.classDescription.containsIgnoreCase(keyword))
                                .or(creator.creatorName.containsIgnoreCase(keyword))
                                .or(creator.creatorDescription.containsIgnoreCase(keyword))
                );

        // 정렬 조건 분기
        switch (sort) {
            case "popular":
                query.leftJoin(like).on(like.classLikeRef.eq(cls))
                        .groupBy(cls.classIdx)
                        .orderBy(like.count().desc());
                break;
            case "old":
                query.orderBy(cls.createdAt.asc());
                break;
            default:
                query.orderBy(cls.createdAt.desc());
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
            } else {
                classResults.forEach(dto -> dto.setLiked(false));
            }
        } else {
            classResults.forEach(dto -> dto.setLiked(false));
        }

        // 전체 개수 (정렬/페이징과 무관하게 동일한 조건으로)
        long total = queryFactory
                .select(cls.count())
                .from(cls)
                .leftJoin(cls.creator, creator)
                .where(
                        cls.classTitle.containsIgnoreCase(keyword)
                                .or(cls.classDescription.containsIgnoreCase(keyword))
                                .or(creator.creatorName.containsIgnoreCase(keyword))
                                .or(creator.creatorDescription.containsIgnoreCase(keyword))
                )
                .fetchOne();

        // 크리에이터 결과
        List<Map<String, String>> creatorResults = queryFactory
                .select(creator.creatorName, creator.creatorProfileImg, creator.creatorDescription)
                .from(cls)
                .leftJoin(cls.creator, creator)
                .where(
                        creator.creatorName.containsIgnoreCase(keyword)
                                .or(creator.creatorDescription.containsIgnoreCase(keyword))
                )
                .distinct()
                .fetch()
                .stream()
                .map(t -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("name", t.get(creator.creatorName));
                    map.put("profileImage", t.get(creator.creatorProfileImg));
                    map.put("description", t.get(creator.creatorDescription));
                    return map;
                })
                .collect(Collectors.toList());

        return Map.of(
                "classes", new PageImpl<>(classResults, pageable, total),
                "creators", creatorResults
        );
    }

    @Override
    public Page<ClassDTO> getPagedClassesByCategoryAndSub(Integer categoryIdx, Integer subCategoryIdx, Pageable pageable, String sort) {
        QClassEntity cls = QClassEntity.classEntity;
        QFileEntity file = QFileEntity.fileEntity;
        QCategoryEntity cat = QCategoryEntity.categoryEntity;
        QSubCategoryEntity sub = QSubCategoryEntity.subCategoryEntity;
        QCreatorEntity creator = QCreatorEntity.creatorEntity;
        QClassLikeEntity like = QClassLikeEntity.classLikeEntity;

        var query = queryFactory
                .select(Projections.constructor(ClassDTO.class,
                        cls.classIdx,
                        cls.classTitle,
                        cls.classDescription,
                        file.filePath,
                        cat.categoryName,
                        creator.creatorName,
                        creator.creatorProfileImg,
                        creator.creatorDescription,
                        cls.createdAt,
                        sub.subCategoryName
                ))
                .from(cls)
                .leftJoin(cls.classThumbnailImg, file)
                .leftJoin(cls.classCategory, cat)
                .leftJoin(cls.classSubCategory, sub)
                .leftJoin(cls.creator, creator)
                .where(
                        cat.categoryIdx.eq(categoryIdx),
                        subCategoryIdx != null ? sub.subCategoryIdx.eq(subCategoryIdx) : null
                );

        switch (sort) {
            case "popular" -> {
                query.leftJoin(like).on(like.classLikeRef.eq(cls))
                        .groupBy(cls.classIdx)
                        .orderBy(like.count().desc());
            }
            case "old" -> query.orderBy(cls.createdAt.asc());
            default -> query.orderBy(cls.createdAt.desc());
        }

        List<ClassDTO> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(cls.count())
                .from(cls)
                .leftJoin(cls.classCategory, cat)
                .leftJoin(cls.classSubCategory, sub)
                .where(
                        cat.categoryIdx.eq(categoryIdx),
                        subCategoryIdx != null ? sub.subCategoryIdx.eq(subCategoryIdx) : null
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }
  
 @Override
    public List<ClassDTO> getWishListByUserId(String userId) {
        if (userId == null) {
            return Collections.emptyList();
        }

        QClassEntity cls = QClassEntity.classEntity;
        QFileEntity file = QFileEntity.fileEntity;
        QCategoryEntity cat = QCategoryEntity.categoryEntity;
        QCreatorEntity creator = QCreatorEntity.creatorEntity;
        QClassLikeEntity like = QClassLikeEntity.classLikeEntity;

        List<ClassDTO> classList = queryFactory
                .select(Projections.constructor(ClassDTO.class,
                        cls.classIdx,
                        cls.classTitle,
                        cls.classDescription,
                        file.filePath,
                        cat.categoryName,
                        creator.creatorName,
                        creator.creatorProfileImg,
                        creator.creatorDescription
                ))
                .from(cls)
                .leftJoin(cls.classThumbnailImg, file)
                .leftJoin(cls.classCategory, cat)
                .leftJoin(cls.creator, creator)
                .innerJoin(like).on(like.classLikeRef.eq(cls))  // leftJoin -> innerJoin
                .where(like.classLikeUser.userId.eq(userId))
                .orderBy(cls.createdAt.desc())
                .fetch();

        classList.forEach(dto -> dto.setLiked(true));

        return classList;
    }

    @Override
    public List<LectureDTO> getLectureHistoryByUserId(String userId) {
        if (userId == null) {
            return Collections.emptyList();
        }

        QLectureEntity lecture = QLectureEntity.lectureEntity;
        QLectureHistoryEntity history = QLectureHistoryEntity.lectureHistoryEntity;

        return queryFactory
                .select(Projections.constructor(LectureDTO.class,
                        lecture.lectureIdx,
                        lecture.lectureTitle,
                        lecture.lectureDurationSec,
                        history.lectureHistoryTotalWatchTime.hour().multiply(3600)
                                .add(history.lectureHistoryTotalWatchTime.minute().multiply(60))
                                .add(history.lectureHistoryTotalWatchTime.second()).sum(),
                        history.lectureHistoryTotalWatchTime.hour().multiply(3600)
                                .add(history.lectureHistoryTotalWatchTime.minute().multiply(60))
                                .add(history.lectureHistoryTotalWatchTime.second()).sum()
                                .multiply(100)
                                .divide(lecture.lectureDurationSec)
                                .castToNum(Integer.class),
                        history.lectureHistoryLastWatchDate.max()
                ))
                .from(history)
                .innerJoin(history.lectureHistoryRef, lecture)
                .where(history.lectureHistoryUser.userId.eq(userId))
                .groupBy(
                        lecture.lectureIdx,
                        lecture.lectureTitle,
                        lecture.lectureDurationSec
                )
                .orderBy(history.lectureHistoryLastWatchDate.max().desc())
                .fetch();
    }

    @Override
    public List<String> getLectureThumbnailsByClassId(Integer classId) {
        QLectureEntity lec = QLectureEntity.lectureEntity;
        QFileEntity file = QFileEntity.fileEntity;

        return queryFactory
                .select(file.filePath)
                .from(lec)
                .leftJoin(lec.lectureThumbnail, file)
                .where(lec.lectureRef.classIdx.eq(classId),
                        file.filePath.isNotNull())
                .orderBy(lec.lectureIdx.asc()) // 또는 원하는 순서 기준
                .limit(5) // 필요 시 제한
                .fetch();
    }


}
