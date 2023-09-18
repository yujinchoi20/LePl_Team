package com.lepl.api.argumentresolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;



@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("supportsParameter 실행");

        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
        boolean hasLongType = Long.class.isAssignableFrom(parameter.getParameterType());
        // 어노테이션이 @Login 이고, 해당 파라미터 타입이 Long 라면 true 반환
        return hasLoginAnnotation && hasLongType;
    }

    // 위 supportsParameter 가 true 면 아래 함수가 실행
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        log.info("resolveArgument 실행");

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(false); // false : 없으면 null
        if(session == null) {
            return null;
        }

        Long memberId = Long.valueOf(session.getAttribute("login_member").toString());
        return memberId;
    }
}
