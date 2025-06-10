package net.fullstack.class101clone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Class101CloneApplication {

    public static void main(String[] args) {
        SpringApplication.run(Class101CloneApplication.class, args);
    }

}
