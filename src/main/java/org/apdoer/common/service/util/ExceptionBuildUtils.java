package org.apdoer.common.service.util;

import org.apdoer.common.service.exception.impl.CheckedException;
import org.apdoer.common.service.exception.impl.TradeException;
import org.apdoer.common.service.exception.impl.UnCheckedException;
import org.apdoer.common.service.exception.impl.UnTradeException;

/**
 * @author qzshen
 * @description 异常类构造工具
 */
public class ExceptionBuildUtils {

    private ExceptionBuildUtils() {
    }

    /**
     * 构造异常类对象
     *
     * @param exceptionCode    异常代码
     * @param exceptionMessage 异常信息
     * @param e                异常栈
     * @param CheckedFlag      是否为检查异常标志
     * @return org.apdoer.common.service.exception 异常对象
     */
    @Deprecated
    public static Exception buildException(int exceptionCode, String exceptionMessage, Throwable e, boolean CheckedFlag) {
        Exception exception = null;
        if (CheckedFlag) {
            exception = new CheckedException(exceptionCode, exceptionMessage, e);
        } else {
            exception = new UnCheckedException(exceptionCode, exceptionMessage, e);
        }

        return exception;
    }

    /**
     * 构造异常类对象
     *
     * @param exceptionCode    异常代码
     * @param exceptionMessage 异常信息
     * @param e                异常栈
     * @return org.apdoer.common.service.exception 异常对象
     */
    public static Exception buildException(int exceptionCode, String exceptionMessage, Throwable e) {
        return buildException(exceptionCode, exceptionMessage, e, true);
    }

    /**
     * 构造检查异常对象
     *
     * @param exceptionCode    异常代码
     * @param exceptionMessage 异常信息
     * @param e                异常栈
     * @return CheckedException 检查异常对象
     */
    @Deprecated
    public static CheckedException buildCheckException(int exceptionCode, String exceptionMessage, Throwable e) {
        return (CheckedException) buildException(exceptionCode, exceptionMessage, e, true);
    }

    /**
     * 构造非检查异常对象
     *
     * @param exceptionCode    异常代码
     * @param exceptionMessage 异常信息
     * @param e                异常栈
     * @return CheckedException 非检查异常对象
     */
    @Deprecated
    public static UnCheckedException buildUnCheckException(int exceptionCode, String exceptionMessage, Throwable e) {
        return (UnCheckedException) buildException(exceptionCode, exceptionMessage, e, false);
    }

    /**
     * 构造交易异常类
     *
     * @param exceptionCode    异常代码
     * @param exceptionMessage 异常消息
     * @return TradeException 交易异常对象
     */
    public static TradeException buildTradeException(int exceptionCode, String exceptionMessage) {
        return new TradeException(exceptionCode, exceptionMessage);
    }

    /**
     * 构造交易异常类
     *
     * @param exceptionCode    异常代码
     * @param exceptionMessage 异常消息
     * @param e                Throwable对象
     * @return TradeException 交易异常对象
     */
    public static TradeException buildTradeException(int exceptionCode, String exceptionMessage, Throwable e) {
        return new TradeException(exceptionCode, exceptionMessage, e);
    }

    /**
     * 构造交易异常类
     *
     * @param exceptionCode    异常代码
     * @param exceptionMessage 异常消息
     * @return TradeException 交易异常对象
     */
    public static UnTradeException buildUnTradeException(int exceptionCode, String exceptionMessage) {
        return new UnTradeException(exceptionCode, exceptionMessage);
    }

    /**
     * 构造交易异常类
     *
     * @param exceptionCode    异常代码
     * @param exceptionMessage 异常消息
     * @param e                Throwable对象
     * @return TradeException 交易异常对象
     */
    public static UnTradeException buildUnTradeException(int exceptionCode, String exceptionMessage, Throwable e) {
        return new UnTradeException(exceptionCode, exceptionMessage, e);
    }

}
