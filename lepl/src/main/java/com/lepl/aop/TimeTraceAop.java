package com.lepl.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect // AOP
@Component // "빈" 등록
@Slf4j
public class TimeTraceAop {

    @Around("execution(* com.lepl..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        // 프록시 실행
        long start = System.currentTimeMillis();
        log.debug("START: {}", joinPoint.toString());
        try {
            return joinPoint.proceed(); // 실제 실행
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            log.debug("END: {} {}ms", joinPoint.toString(), timeMs);
        }
    }
}
