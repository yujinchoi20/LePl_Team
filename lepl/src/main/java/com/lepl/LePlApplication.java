package com.lepl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching // 캐시 사용
@EnableScheduling // 스케줄링 사용
@SpringBootApplication
public class LePlApplication {
	public static void main(String[] args) {
		SpringApplication.run(LePlApplication.class, args);
	}

}