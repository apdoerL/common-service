package org.apdoer.common.service.annotation;

import java.lang.annotation.*;


/**
 * 定时任务
 * @author apdoer
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ScheduledLog {

    String description() default "";
}
