package com.dongsw.authority.common.util;

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
	PERMISSION_DENIED(30007, "Permission_denied"),
	INVALID_URI(30008,"Invalid URI" ),
	RESOURCE_NOT_EXIST(30009, "URI_NOT_EXIST");

	private int errcode;
	private String errmsg;

	private ResultStatusCode(int errode, String errMsg) {
		this.errcode = errode;
		this.errmsg = errMsg;
	}

	public int getErrcode() {
		return errcode;
	}


	public String getErrmsg() {
		return errmsg;
	}
}