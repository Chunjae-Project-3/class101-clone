//package net.fullstack.class101clone.repository.classes;
//
//import net.fullstack.class101clone.domain.ClassEntity;
//import net.fullstack.class101clone.dto.ClassDTO;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.EntityGraph;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.repository.query.Param;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface ClassRepository extends JpaRepository<ClassEntity, Integer>, ClassRepositoryCustom {
//
//    @EntityGraph(attributePaths = {"creator"})
//    Optional<ClassEntity> findWithCreatorByClassIdx(Integer classIdx);
//
//}
