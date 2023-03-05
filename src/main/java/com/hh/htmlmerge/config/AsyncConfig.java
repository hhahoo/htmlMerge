package com.hh.htmlmerge.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {
    private static int CorePoolSize = 10;
    private static int QueueCapacity = 100;
    private static int MaxPoolSize = 200;
    private static String ThreadNamePrefix = "crawling-task";

    @Bean
    public Executor crawlingTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CorePoolSize);
        executor.setQueueCapacity(QueueCapacity);
        executor.setMaxPoolSize(MaxPoolSize);
        executor.setThreadNamePrefix(ThreadNamePrefix);
        executor.initialize();

        return executor;
    }
}
