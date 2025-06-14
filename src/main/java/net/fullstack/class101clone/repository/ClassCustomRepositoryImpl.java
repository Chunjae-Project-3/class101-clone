package net.fullstack.class101clone.repository;

import com.querydsl.jpa.JPQLQuery;
import net.fullstack.class101clone.domain.*;
import net.fullstack.class101clone.exception.NotFoundException;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class ClassCustomRepositoryImpl extends QuerydslRepositorySupport implements ClassCustomRepository {

    public ClassCustomRepositoryImpl() {
        super(ClassEntity.class);
    }

    @Override
    public ClassEntity getCurriculumByClassIdx(int classIdx) {
        QClassEntity classQ = QClassEntity.classEntity;
        QSectionEntity sectionQ = QSectionEntity.sectionEntity;
        QLectureEntity lectureQ = QLectureEntity.lectureEntity;
        QSectionFileEntity sectionFileQ = QSectionFileEntity.sectionFileEntity;

        ClassEntity result = from(classQ)
                .leftJoin(classQ.sectionList, sectionQ)
                .leftJoin(sectionQ.lectureList, lectureQ)
                .leftJoin(sectionQ.sectionFileList, sectionFileQ)
                .where(classQ.classIdx.eq(classIdx))
                .orderBy(sectionQ.sectionOrder.asc(), lectureQ.lectureIdx.asc())
                .fetchOne();

        if (result == null) {
            throw new NotFoundException("Curriculum not found. id: " + classIdx);
        }

        return result;
    }
}
