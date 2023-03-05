package com.hh.htmlmerge.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/** 멀티쓰레드 구현을 위한 Async Configuration */
@Configuration
@EnableAsync
public class AsyncConfig {
    private static int CorePoolSize = 10; // 기본 Thread 수
    private static int QueueCapacity = 10; // Thread 대기 큐 용량
    private static int MaxPoolSize = 200; // 최대 Thread Pool 수
    private static String ThreadNamePrefix = "crawling-task"; // Thread 이름 접두사

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
