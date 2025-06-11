package net.fullstack.class101clone.repository.classes;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.fullstack.class101clone.domain.QCategoryEntity;
import net.fullstack.class101clone.domain.QClassEntity;
import net.fullstack.class101clone.domain.QFileEntity;
import net.fullstack.class101clone.dto.ClassDTO;

import java.util.List;

@RequiredArgsConstructor
public class ClassRepositoryImpl implements ClassRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ClassDTO> getClasses(String categoryName) {
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
                        )
                )
                .from(cls)
                .leftJoin(cls.classThumbnailImg, file)
                .leftJoin(cls.classCategory, cat)
                .where(
                        categoryName != null ? cat.categoryName.eq(categoryName) : null
                )
                .orderBy(cls.createdAt.desc())
                .fetch();
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
