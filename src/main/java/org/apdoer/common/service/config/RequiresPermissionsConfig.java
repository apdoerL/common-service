package org.apdoer.common.service.config;

import org.apdoer.common.service.interceptor.RequiresIdempotencyInterceptor;
import org.apdoer.common.service.interceptor.RequiresPermissionsInterceptorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * springboot2.+ WebMvcConfigurerAdapter过时
 */
@Configuration
public class RequiresPermissionsConfig implements WebMvcConfigurer {

    @Autowired
    private RequiresPermissionsInterceptorService interceptor;

    @Autowired
    private RequiresIdempotencyInterceptor requiresIdempotencyInterceptor;

    public RequiresPermissionsInterceptorService requiresPermissionsInterceptorService() {
        return interceptor;
    }

    public RequiresIdempotencyInterceptor requiresIdempotencyInterceptor() {
        return requiresIdempotencyInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(requiresPermissionsInterceptorService())
                .excludePathPatterns("/static/*")
                .excludePathPatterns("/org/apdoer/common/service/common/*")
                .excludePathPatterns("/error")
                .addPathPatterns("/**");
        registry
                .addInterceptor(requiresIdempotencyInterceptor())
                .excludePathPatterns("/static/*")
                .excludePathPatterns("/org/apdoer/common/service/common/*")
                .excludePathPatterns("/error")
                .addPathPatterns("/**");
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
