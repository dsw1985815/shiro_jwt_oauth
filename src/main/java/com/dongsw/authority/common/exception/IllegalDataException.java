package com.dongsw.authority.common.exception;

/**
 * Comments：
 * Author：dongshuangwei
 * Create Date：2017/2/28
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version：v1.0
 */
public class IllegalDataException extends Exception {

    public IllegalDataException(String message) {
        super(message);
    }

    public IllegalDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalDataException(Throwable cause) {
        super(cause);
    }

    public IllegalDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
