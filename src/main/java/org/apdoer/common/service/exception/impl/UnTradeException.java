package org.apdoer.common.service.exception.impl;

/**
 * @author qzshen
 * @description 非交易业务异常类
 */
public class UnTradeException extends UnCheckedException{


	private static final long serialVersionUID = 3104420803803699099L;

	public UnTradeException(int exceptionCode, String exceptionMessage) {
		super(exceptionCode, exceptionMessage);
	}
	
	public UnTradeException(int exceptionCode, String exceptionMessage, Throwable e) {
		super(exceptionCode, exceptionMessage, e);
	}
}
