package com.example.backend.aspect;

import cn.hutool.core.date.DateTime;
import com.example.backend.annotation.LogOperation;
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
public class OperationLogAspect {

    @Autowired
    private OperationLogMapper operationLogMapper;
    private static final Logger logger = LoggerFactory.getLogger(OperationLogAspect.class);

    // 定义切入点，匹配所有带有 @LogOperation 注解的方法
    @Pointcut("@annotation(com.example.backend.annotation.LogOperation)")
    public void logOperation() {}
    // 在方法成功返回后记录日志
    @AfterReturning("logOperation()")
    public void afterReturning(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        // 可以从方法参数中提取更多信息，如当前用户、操作类型等
        // 这里只是一个简单的示例
        logger.info("Method: {} was called with arguments: {}", methodName, args);

        // 获取注解上的描述信息
        LogOperation annotation = ((org.aspectj.lang.reflect.MethodSignature)joinPoint.getSignature()).getMethod().getAnnotation(LogOperation.class);
        if (annotation != null && !annotation.value().isEmpty()) {
            logger.info("Operation description: {}", annotation.value());
        }
        // 添加代码将日志信息保存到数据库或文件中
        OperationLog logEntry = new OperationLog();
        logEntry.setUser_id(1);//管理员id
        logEntry.setContent(annotation.value());
        logEntry.setDate(new DateTime().toString());
        operationLogMapper.insert(logEntry);
    }
}