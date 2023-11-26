package com.lepl.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {

    @Override
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("members");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .initialCapacity(50) // 내부 해시 테이블의 최소한의 크기
                .maximumSize(50) // 캐시에 포함할 수 있는 최대 엔트리 수
//                .weakKeys() // 직접 키를 설정하므로 주석처리
                .recordStats());
        return cacheManager;
    }
}
