package com.example.backend.aspect;

import cn.hutool.core.date.DateTime;
import com.example.backend.annotation.LogOperationWithId;
import com.example.backend.entity.OperationLog;
import com.example.backend.mapper.OperationLogMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class OperationLogWithIdAspect {
    @Autowired
    private OperationLogMapper operationLogMapper;
    private static final Logger logger = LoggerFactory.getLogger(OperationLogWithIdAspect.class);

    // 定义切入点，匹配所有带有 @LogOperationWithId 注解的方法
    @Pointcut("@annotation(com.example.backend.annotation.LogOperationWithId)")
    public void logOperationWithId() {}

    @AfterReturning("logOperationWithId()")
    public void afterReturning(JoinPoint joinPoint) {
        afterReturningCommon(joinPoint, LogOperationWithId.class);
    }

    private void afterReturningCommon(JoinPoint joinPoint, Class<LogOperationWithId> annotationClass) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        // 获取注解上的描述信息和用户ID
        Object annotation = ((org.aspectj.lang.reflect.MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(annotationClass);
        if (annotation != null && annotationClass.isInstance(annotation)) {
            LogOperationWithId logOperation = (LogOperationWithId) annotation;
            String operationDescription = logOperation.value();
            int index = logOperation.idParamIndex();
            int userId = (int) args[index]; // 从注解中获取用户ID
            logger.info("Method: {} was called with arguments: {}", methodName, args);
            logger.info("User ID: {}: Operation description: {}", userId,operationDescription);
            // 创建一个新的日志条目并保存到数据库
            OperationLog logEntry = new OperationLog();
            logEntry.setUser_id(userId); // 使用从注解中获取的用户ID
            logEntry.setContent(operationDescription);
            logEntry.setDate(new DateTime().toString());
            operationLogMapper.insert(logEntry);
        }
    }
}
