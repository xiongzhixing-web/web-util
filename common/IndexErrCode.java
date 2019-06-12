package com.wisdom.util.common;

public class IndexErrCode {
    private final static int ERR_PARAM_INVALID = 301;
    private final static int ERR_BIZ_DATA_EXCEPTION = 401;
    private final static int ERR_RPC_INVOKE_FAIL = 402;
    private final static int ERR_CATCH_EXCEPTION = 403;

    public static <T extends EmptyResp> T error(T resp,long errCode,String errMessage){
        resp.setErrCode(errCode);
        resp.setErrMessage(errMessage);
        return resp;
    }



}
