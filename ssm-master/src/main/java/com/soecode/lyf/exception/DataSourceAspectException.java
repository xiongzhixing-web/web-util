package com.soecode.lyf.exception;

/**
 * 自定义异常，用于捕捉数据库主从
 * @author wanghongwei
 *
 * 2018年10月11日-上午11:09:40
 */
public class DataSourceAspectException extends Exception{
	
	private static final long serialVersionUID = -8717735752896211965L;

	private final String userErrMsg;

	public DataSourceAspectException(String userErrMsg, Throwable e) {
		super(userErrMsg, e);
		this.userErrMsg=userErrMsg;
	}

	public String getUserErrMsg() {
		return userErrMsg;
	}
	

}
