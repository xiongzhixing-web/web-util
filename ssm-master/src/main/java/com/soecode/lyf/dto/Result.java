package com.soecode.lyf.dto;

import com.soecode.lyf.annotation.DocAnnotation;

/**
 * 封装json对象，所有返回结果都使用它
 */
public class Result<T> {
	@DocAnnotation(comment = "错误码，200表示成功")
	private long errCode;// 错误码
	@DocAnnotation(comment = "错误消息")
	private String errMessage;// 错误信息
    @DocAnnotation(comment = "响应数据数据")
	private T data;// 错误信息

	public static <T> Result<T> success(T data){
		Result<T> result = new Result<T>();
		result.setData(data);
		return result;
	}

	public static <T> Result<T> fail(long errCode,String errMessage){
		Result<T> result = new Result<T>();
		result.setErrCode(errCode);
		result.setErrMessage(errMessage);
		return result;
	}

	public long getErrCode() {
		return errCode;
	}

	public void setErrCode(long errCode) {
		this.errCode = errCode;
	}

	public String getErrMessage() {
		return errMessage;
	}

	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
