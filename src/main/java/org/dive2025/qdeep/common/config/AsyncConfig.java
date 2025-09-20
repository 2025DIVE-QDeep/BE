package org.dive2025.qdeep.common.config;


import org.springframework.boot.task.ThreadPoolTaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@EnableAsync
@Configuration
public class AsyncConfig {

    @Bean(name = "gptTaskExecutor")
    public Executor asyncExecutor(ThreadPoolTaskExecutorBuilder builder){
        return builder
                .corePoolSize(20)
                .maxPoolSize(100)
                .queueCapacity(500)
                .keepAlive(Duration.ofSeconds(60))
                .threadNamePrefix("gptTaskExecutor")
                .build();
    }
}
