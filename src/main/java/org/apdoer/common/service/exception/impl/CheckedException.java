package org.apdoer.common.service.exception.impl;

import org.apdoer.common.service.exception.Exceptionable;

import java.io.PrintWriter;
import java.io.StringWriter;


/**
 * 
 * @author qzshen
 * 
 * @description 检测异常类   
 */
public class CheckedException extends Exception implements Exceptionable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1120422087740678906L;
	
	//异常代码
	protected final int exceptionCode;
	//异常信息
	protected final String exceptionMessage;
	
	protected Throwable e;
	
	
	public CheckedException(int exceptionCode, String exceptionMessage) {
		this(exceptionCode, exceptionMessage, null);
	}
	
	public CheckedException(int exceptionCode, String exceptionMessage, Throwable e) {
		this.exceptionCode = exceptionCode;
		this.exceptionMessage = exceptionMessage;
		this.e = e;
	}

	@Override
	public int getExceptionCode() {
		return this.exceptionCode;
	}

	@Override
	public String getExceptionMessage() {
		return this.exceptionMessage;
	}

	@Override
	public Throwable getThrowable() {
		return this.e;
	}

	@Override
	public String getStackTraceInfo() {
		StringWriter sw = new StringWriter();
		sw.write("exceptionCode:"+this.exceptionCode+", exceptionMessage:"+exceptionMessage+System.getProperty("line.separator"));
		if (null != getThrowable()) {
			getThrowable().printStackTrace(new PrintWriter(sw));
		}
		return sw.toString();
	}

}
