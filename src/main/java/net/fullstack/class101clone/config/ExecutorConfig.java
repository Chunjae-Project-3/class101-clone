package net.fullstack.class101clone.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log4j2
@Configuration
public class ExecutorConfig {
    @Bean
    public ExecutorService videoExecutor() {
        return Executors.newFixedThreadPool(2);
    }
}
