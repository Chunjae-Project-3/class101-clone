//package net.fullstack.class101clone.repository;
//
//
//import net.fullstack.api.domain.BbsEntity;
//import net.fullstack.api.repository.bbs.BbsSearch;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//
//public interface BbsRepository extends JpaRepository<BbsEntity, Long>, BbsSearch {
//    @Query(value = "select count(idx) from tbl_board", nativeQuery = true)
//    int getTotalCount();
//
//
//}
