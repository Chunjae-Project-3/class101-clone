package net.fullstack.class101clone.repository;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import net.fullstack.class101clone.repository.classes.ClassRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Log4j2
@SpringBootTest
@Transactional
public class ClassRepositoryTests {
    @Autowired
    private ClassRepository classRepository;

    @Test
    public void testFindAll() {
        log.info("==================================================");
        log.info("ClassRepositoryTests >> testFindAll >> result: {}", classRepository.findAll());
        log.info("==================================================");
    }
}
