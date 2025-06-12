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

import java.util.List;

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
    public Page<ClassDTO> getPagedClassesByCategoryIdx(Integer categoryIdx, Pageable pageable) {
        QClassEntity cls = QClassEntity.classEntity;
        QFileEntity file = QFileEntity.fileEntity;
        QCategoryEntity cat = QCategoryEntity.categoryEntity;
        QCreatorEntity creator = QCreatorEntity.creatorEntity;

        List<ClassDTO> content = queryFactory
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
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // count 쿼리
        long total = queryFactory
                .select(cls.count())
                .from(cls)
                .leftJoin(cls.classCategory, cat)
                .where(cat.categoryIdx.eq(categoryIdx))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
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
