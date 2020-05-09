package org.apdoer.common.service.distributionlock.annotation;

import java.lang.annotation.*;

/**
 * @author li
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Documented
public @interface LockKey {
}
