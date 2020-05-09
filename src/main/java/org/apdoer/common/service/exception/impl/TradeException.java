package org.apdoer.common.service.exception.impl;

/**
 * @author qzshen
 * @description 交易业务异常类
 */
public class TradeException extends UnCheckedException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1276442684405755014L;

	public TradeException(int exceptionCode, String exceptionMessage) {
		super(exceptionCode, exceptionMessage);
	}
	
	public TradeException(int exceptionCode, String exceptionMessage, Throwable e) {
		super(exceptionCode, exceptionMessage, e);
	}

}
