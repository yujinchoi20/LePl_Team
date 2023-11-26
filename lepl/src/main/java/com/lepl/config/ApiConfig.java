package com.lepl.config;

import com.lepl.api.argumentresolver.LoginMemberArgumentResolver;
import com.lepl.api.interceptor.MemberCheckInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;
import java.util.List;

@Configuration // 설정 파일임을 알림
@Slf4j
public class ApiConfig implements WebMvcConfigurer {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MemberCheckInterceptor())
                .order(2)
                .addPathPatterns("/**") // 모든 경로 접근
                .excludePathPatterns("/", "/api/v1/members/login", "/api/v1/members/register",
                        "/api/v1/members/logout", "/api/v1/members/*",
                        "/image/**", "/css/**", "/*.ico", "/error"); // 제외 경로!

    }

    // 정적이미지 경로 핸들링 + 캐시
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        CacheControl cacheControl = CacheControl.maxAge(Duration.ofDays(365));
        registry.addResourceHandler("/image/**")
                .addResourceLocations("file:///C:/images-spring/") // LF 조심
                .setCacheControl(cacheControl); // 브라우저 캐시 추가
//                .addResourceLocations("file:///var/www/images-spring/");
    }

    // CORS
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:9000");
    }
}
