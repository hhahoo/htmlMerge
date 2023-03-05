package com.hh.htmlmerge.config;

import java.time.Duration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

/** Caffeine 로컬 캐시 적용을 위한 캐시 메니저 Configuration */
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CaffeineCacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine( Caffeine.newBuilder()
                                    .expireAfterWrite(Duration.ofHours(1)) // TTL은 1시간으로 설정
                                    .weakValues() ); // Strong 참조가 없는 경우 Garbage Collector에 의해 정리될 수 있도록 설정
        return cacheManager;
    }
    
}
