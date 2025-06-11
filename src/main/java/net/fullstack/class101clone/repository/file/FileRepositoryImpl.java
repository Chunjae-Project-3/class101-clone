package net.fullstack.class101clone.repository.file;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.fullstack.class101clone.domain.QLectureEntity;
import net.fullstack.class101clone.domain.QFileEntity;

import java.util.List;

@RequiredArgsConstructor
public class FileRepositoryImpl implements FileRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<String> findImagePathsByClassId(Integer classId) {
        QLectureEntity lec = QLectureEntity.lectureEntity;
        QFileEntity file = QFileEntity.fileEntity;

        return queryFactory
                .select(file.filePath)
                .from(lec)
                .join(lec.lectureVideo, file)
                .where(lec.lectureRef.classIdx.eq(classId))
                .fetch();
    }
}
