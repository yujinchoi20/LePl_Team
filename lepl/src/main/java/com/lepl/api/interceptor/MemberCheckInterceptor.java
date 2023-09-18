package com.lepl.api.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;


@Slf4j // log
public class MemberCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        log.info("인증 체크 인터셉터 실행 {}", requestURI);

        HttpSession session = request.getSession();
        if(session == null || session.getAttribute("login_member")==null) {
            log.info("미인증 사용자 요청");
            // 회원 아님을 알림
            response.setStatus(HttpStatus.UNAUTHORIZED.value()); // status code : 401
            return false;
        }
        return true;
    }
}
