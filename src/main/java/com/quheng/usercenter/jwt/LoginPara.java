/**
 * Comments：
 * Author：dongshuangwei
 * Create Date：2017/2/16
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version：v1.0
 */
package com.quheng.usercenter.jwt;

public class LoginPara {
    private String clientId;
    private String userName;
    private String password;
    private String captchaCode;
    private String captchaValue;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCaptchaCode() {
        return captchaCode;
    }

    public void setCaptchaCode(String captchaCode) {
        this.captchaCode = captchaCode;
    }

    public String getCaptchaValue() {
        return captchaValue;
    }

    public void setCaptchaValue(String captchaValue) {
        this.captchaValue = captchaValue;
    }
}