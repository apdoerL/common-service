package org.apdoer.common.service.annotation;

import java.lang.annotation.*;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/4/24 19:24
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresIdempotency {

    /**
     * 过期时间 秒
     *
     */
    int expire() default 3600;
}
