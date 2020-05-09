package org.apdoer.common.service.distributionlock.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author apdoer
 * @version 1.0
 * @date 2019/12/2 15:18
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    String prefix() default "";

    int expireTime() default 60;

    int tryCount() default 3;

    int waitMillis() default 1;

    TimeUnit timeunit() default TimeUnit.SECONDS;
}
