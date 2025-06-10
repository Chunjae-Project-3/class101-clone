//package net.fullstack.class101clone.repository.bbs;
//
//import net.fullstack.api.domain.BbsReplyEntity;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//public interface BbsReplyRepository extends JpaRepository<BbsReplyEntity, Long> {
//    @Query(value = "select count(idx) from tbl_reply_board", nativeQuery = true)
//    long getTotalCount();
//
//    @Query(value = "SELECT COUNT(*) FROM tbl_board_reply WHERE board_idx = :board_idx", nativeQuery = true)
//    long countByBoardIdx(@Param("board_idx") Long board_idx);
//
//    @Query("select r from BbsReplyEntity r where r.board.idx = :board_idx")
//    Page<BbsReplyEntity> list(@Param("board_idx") Long board_idx, Pageable pageable);
//}
