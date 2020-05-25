package org.apdoer.common.service.distributionlock.advice;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apdoer.common.service.distributionlock.RedisLock;
import org.apdoer.common.service.distributionlock.RedisUtil;
import org.apdoer.common.service.distributionlock.annotation.DistributedLock;
import org.apdoer.common.service.distributionlock.annotation.LockKey;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.stream.Stream;

/**
 * @author apdoer
 * @version 1.0
 * @date 2019/12/2 15:22
 */
@AllArgsConstructor
@Component
@Aspect
@Slf4j
public class DistributedLockAdvice {

    public static final String PREFIX = "org:apdoer:distributionLock:";

    final RedisUtil redisUtil;

    @Pointcut("@annotation(org.apdoer.common.service.distributionlock.annotation.DistributedLock)")
    public void lockAspect() {
    }


    @Around("lockAspect()")
    @Transactional(rollbackFor = Exception.class)
    public Object lockAroundAction(ProceedingJoinPoint joinPoint) throws Throwable {
        Class<?> type = joinPoint.getSignature().getDeclaringType();
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        DistributedLock lock = method.getAnnotation(DistributedLock.class);

        String prefix = PREFIX + StringUtils.defaultIfBlank(lock.prefix(), type.getName() + ":" + method.getName());

        RedisLock redisLock = new RedisLock(redisUtil, prefix, lock.expireTime(), lock.timeunit(), lock.waitMillis(), lock.tryCount());
        boolean isLock = redisLock.getLock();
        if (!isLock) {
            log.error("get lock fail,waiting....");
            throw new RuntimeException("get lock fail, please wait " + redisLock.getWaitSecond() + "seconds..");
        }
        Object result;
        try {
            result = joinPoint.proceed();
        } finally {
            redisLock.release();
        }
        return result;
    }


    private String getKey(Method method, Object[] args) {
        Annotation[][] annotations = method.getParameterAnnotations();
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < annotations.length; i++) {
            if (Stream.of(annotations[i]).anyMatch(o -> o.annotationType().isAssignableFrom(LockKey.class))) {
                key.append(args[i]);
            }
        }
        return key.toString();
    }

}
