package org.apdoer.common.service.exception;

/**
 * 异常接口
 *
 * @author qzshen.oth
 */
public interface Exceptionable {

    /**
     * 获取异常代码
     *
     * @return int
     */
    int getExceptionCode();

    /**
     * 获取异常信息
     *
     * @return String
     */
    String getExceptionMessage();

    /**
     * 获取throwable
     *
     * @return Throwable
     */
    Throwable getThrowable();

    /**
     * 获取异常栈信息
     *
     * @return String
     */
    String getStackTraceInfo();


}
