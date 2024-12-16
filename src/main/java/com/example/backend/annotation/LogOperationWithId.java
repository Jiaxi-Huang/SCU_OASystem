package com.example.backend.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogOperationWithId {
    String value(); // 操作描述
    int idParamIndex(); // 传递 id 的参数名称
}