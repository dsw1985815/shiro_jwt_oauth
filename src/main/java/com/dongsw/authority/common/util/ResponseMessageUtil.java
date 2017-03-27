package com.dongsw.authority.common.util;

import com.dongsw.authority.common.def.ResultCode;
import com.dongsw.authority.common.exception.RespExcption;

/**
 * 断言类,用于快速处理校验并抛出运行时异常,方便事物回滚
 *
 * @author 戚羿辰
 * @desc
 * @Date 2017/01/07
 */
public class ResponseMessageUtil {

    private ResponseMessageUtil() {
    }

    /**
     * 断言不为空
     *
     * @throws
     * @author 戚羿辰
     * @Date 2017/01/07
     */
    public static void whenNullReturn(Object obj, String msg) {
        if (null == obj) {
            throw new RespExcption(ResultCode.FAILED, msg);
        }
    }

    /**
     * 断言相等
     *
     * @throws
     * @author 戚羿辰
     * @Date 2017/01/07
     */
    public static void whenTrueReturn(boolean bool, String msg) {
        if (bool) {
            throw new RespExcption(ResultCode.FAILED, msg);
        }
    }

    /**
     * 断言相等
     *
     * @throws
     * @author 戚羿辰
     * @Date 2017/01/07
     */
    public static void whenTrueReturn(boolean bool, int code) {
        if (bool) {
            throw new RespExcption(code, ResultCode.MSG_PARAMS_NULL);
        }
    }

    /**
     * 断言相等
     *
     * @throws
     * @author 戚羿辰s
     * @Date 2017/01/07
     */
    public static void whenTrueReturn(boolean bool, int code, String message) {
        if (bool) {
            throw new RespExcption(code, message);
        }
    }

    /**
     * 断言相等
     *
     * @throws
     * @author 戚羿辰
     * @Date 2017/01/07
     */
    public static void whenTrueReturn(boolean bool) {
        if (bool) {
            throw new RespExcption();
        }
    }

}