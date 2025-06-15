package net.fullstack.class101clone.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import net.fullstack.class101clone.domain.*;
import net.fullstack.class101clone.dto.classes.LectureDTO;
import net.fullstack.class101clone.dto.classes.SectionDTO;
import net.fullstack.class101clone.dto.file.FileDTO;
import net.fullstack.class101clone.exception.NotFoundException;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

public class ClassCustomRepositoryImpl extends QuerydslRepositorySupport implements ClassCustomRepository {

    private final JPAQueryFactory queryFactory;

    public ClassCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        super(ClassEntity.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public List<SectionDTO> getSectionsByClassIdx(int classIdx) {
        QSectionEntity sectionQ = QSectionEntity.sectionEntity;

        List<SectionDTO> result = queryFactory
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

        if (result.isEmpty()) {
            throw new NotFoundException("Sections not found. id: " + classIdx);
        }

        return result;
    }

    @Override
    public List<SectionDTO> getSectionsWithFilesByClassIdx(int classIdx) {
        QSectionEntity sectionQ = QSectionEntity.sectionEntity;
        QSectionFileEntity sectionFileQ = QSectionFileEntity.sectionFileEntity;
        QFileEntity fileQ = QFileEntity.fileEntity;

        List<SectionDTO> result = queryFactory
                .from(sectionQ)
                .leftJoin(sectionQ.sectionFileList, sectionFileQ)
                .leftJoin(sectionFileQ.file, fileQ)
                .where(sectionQ.sectionRef.classIdx.eq(classIdx))
                .orderBy(sectionQ.sectionOrder.asc(), sectionFileQ.ord.asc())
                .transform(
                        groupBy(sectionQ.sectionIdx).list(
                              Projections.bean(SectionDTO.class,
                                      sectionQ.sectionIdx,
                                      sectionQ.sectionRef.classIdx.as("sectionRefIdx"),
                                      sectionQ.sectionTitle,
                                      sectionQ.sectionOrder,
                                      list(Projections.bean(FileDTO.class,
                                              fileQ.fileIdx,
                                              fileQ.fileName,
                                              fileQ.fileExt,
                                              fileQ.filePath,
                                              fileQ.fileSize,
                                              fileQ.fileOrgName
                                      )).as("sectionThumbnailList")
                              )
                        )
                );

        if (result.isEmpty()) {
            throw new NotFoundException("Sections not found. id: " + classIdx);
        }

        return result;
    }

    @Override
    public List<LectureDTO> getLecturesBySectionIdx(int sectionIdx) {
        QLectureEntity lectureQ = QLectureEntity.lectureEntity;
        QFileEntity fileQ = QFileEntity.fileEntity;

        List<LectureDTO> result = queryFactory
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

        if (result.isEmpty()) {
            throw new NotFoundException("Lectures not found. id: " + sectionIdx);
        }

        return result;
    }
}
