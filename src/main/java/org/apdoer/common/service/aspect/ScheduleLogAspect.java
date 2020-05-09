package org.apdoer.common.service.aspect;

import org.apdoer.common.service.annotation.ScheduledLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * 定时任务aop
 *
 * @author apdoer
 * @version 1.0
 * @date 2020/4/26 10:14
 */
@Slf4j
@Component
@Aspect
public class ScheduleLogAspect {

    @Pointcut("@annotation(org.apdoer.common.service.annotation.ScheduledLog)")
    public void logPointCut() {
    }

    @Around("logPointCut() && @annotation(scheduledLog)")
    public Object around(ProceedingJoinPoint joinPoint, ScheduledLog scheduledLog) {
        // 获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getDeclaringTypeName() + "." + signature.getName();

        log.info(">>> Task start. Description: {}, MethodName: {}.", scheduledLog.description(), methodName);
        long start = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            log.error("around notice error.", throwable);
        }
        log.info("<<< Task end. Description: {}, MethodName: {}. TimeConsume: {}ms.", scheduledLog.description(), methodName, System.currentTimeMillis() - start);
        return null;
    }
}
