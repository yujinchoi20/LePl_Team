package com.lepl.api.argumentresolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) // 어노테이션이 메서드의 파라미터에만 적용
@Retention(RetentionPolicy.RUNTIME) // 어노테이션의 수명을 RUNTIME 까지 살아남게 설정
public @interface Login {
}
