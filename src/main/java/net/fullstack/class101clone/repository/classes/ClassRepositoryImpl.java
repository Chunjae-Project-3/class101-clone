package net.fullstack.class101clone.repository.classes;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.fullstack.class101clone.domain.*;
import net.fullstack.class101clone.dto.ClassDTO;
import net.fullstack.class101clone.repository.login.UserRepositoryIf;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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

    @Override
    public ClassDTO getClassDetailById(Integer classId) {
        QClassEntity cls = QClassEntity.classEntity;
        QFileEntity file = QFileEntity.fileEntity;
        QCategoryEntity cat = QCategoryEntity.categoryEntity;

        return queryFactory
                .select(Projections.constructor(ClassDTO.class,
                        cls.classIdx,
                        cls.classTitle,
                        cls.classDescription,
                        file.filePath,
                        cat.categoryName
                ))
                .from(cls)
                .leftJoin(cls.classThumbnailImg, file)
                .leftJoin(cls.classCategory, cat)
                .where(cls.classIdx.eq(classId))
                .fetchOne();
    }

}
