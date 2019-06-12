package com.wisdom.util.common;

/**
 * 响应基类
 */
public class EmptyResp {
    private long errCode;
    private String errMessage;

    public EmptyResp(long errCode, String errMessage) {
        this.errCode = errCode;
        this.errMessage = errMessage;
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
}
