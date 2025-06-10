package net.fullstack.class101clone.repository;

import lombok.extern.log4j.Log4j2;
import net.fullstack.class101clone.domain.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Log4j2
@SpringBootTest
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testUserRegist() {
        log.info("==================================================");
        UserEntity entity = UserEntity.builder()
                .userId("test1")
                .userPwd("1234")
                .userName("test")
                .build();
        UserEntity result = userRepository.save(entity);
        log.info("testUserRegist result: {}", result);
        log.info("==================================================");
    }
}
