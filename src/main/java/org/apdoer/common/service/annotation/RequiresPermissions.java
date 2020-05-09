package org.apdoer.common.service.annotation;

import org.apdoer.common.service.enums.Logical;

import java.lang.annotation.*;

/**
 * @author apdoer
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresPermissions {

    String [] value();

    /**
     * 默认权限 与 的关系
     */
    Logical logical() default Logical.AND;


}
