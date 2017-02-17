package com.quheng.usercenter.utils;

/**
 * Comments：
 * Author：dongshuangwei
 * Create Date：2017/2/16
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version：v1.0
 */

public enum ResultStatusCode {
	OK(0, "OK"),
	SYSTEM_ERR(30001, "System error"),
	INVALID_CLIENTID(30003, "Invalid clientid"),
	INVALID_PASSWORD(30004, "User name or password is incorrect"),
	INVALID_CAPTCHA(30005, "Invalid captcha or captcha overdue"),
	INVALID_TOKEN(30006, "Invalid token"),
	PERMISSION_DENIED(30007, "Permission_denied");

	private int errcode;
	private String errmsg;

	private ResultStatusCode(int Errode, String ErrMsg) {
		this.errcode = Errode;
		this.errmsg = ErrMsg;
	}

	public int getErrcode() {
		return errcode;
	}

	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
}