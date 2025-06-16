package net.fullstack.class101clone.repository.classes;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.fullstack.class101clone.domain.QLectureEntity;
import net.fullstack.class101clone.domain.QLectureHistoryEntity;
import net.fullstack.class101clone.domain.QUserEntity;
import net.fullstack.class101clone.dto.classes.LectureHistoryDTO;

@RequiredArgsConstructor
public class LectureHistoryRepositoryImpl implements LectureHistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public LectureHistoryDTO getLectureHistoryByLectureId(String userId, int lectureIdx) {
        QLectureHistoryEntity historyQ = QLectureHistoryEntity.lectureHistoryEntity;
        QLectureEntity lectureQ = QLectureEntity.lectureEntity;
        QUserEntity userQ = QUserEntity.userEntity;

        return queryFactory
                .select(Projections.bean(LectureHistoryDTO.class,
                        lectureQ.lectureIdx,
                        historyQ.lectureHistoryLastPosition.as("lastPosition"),
                        historyQ.lectureHistoryTotalWatchTime.as("totalWatchTime"),
                        historyQ.lectureHistoryLastWatchDate.as("lastWatchDate")
                ))
                .from(historyQ)
                .innerJoin(historyQ.lectureHistoryRef, lectureQ)
                .innerJoin(historyQ.lectureHistoryUser, userQ)
                .where(
                        historyQ.lectureHistoryUser.userId.eq(userId)
                                .and(historyQ.lectureHistoryRef.lectureIdx.eq(lectureIdx))
                )
                .fetchFirst();
    }
}
