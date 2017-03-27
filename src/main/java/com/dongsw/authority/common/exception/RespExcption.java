package com.dongsw.authority.common.exception;

import com.dongsw.authority.common.def.ResultCode;

/**
 * 自定义异常类，用于直接返回带错误编码的exception
 *
 * @author 戚羿辰
 * @desc
 * @Date 2017/01/07
 */
public class RespExcption extends RuntimeException {

    private static final long serialVersionUID = 5390323631004190156L;

    private final int code;
    private final String message;

    public RespExcption() {
        this.code = ResultCode.FAILED;
        this.message = ResultCode.MSG_PARAMS_NULL;
    }

    public RespExcption(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
