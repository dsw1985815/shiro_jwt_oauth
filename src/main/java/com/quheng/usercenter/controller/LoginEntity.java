package com.quheng.usercenter.controller;

/**
 * Comments：
 * Author：dongshuangwei
 * Create Date：2017/2/20
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version：v1.0
 */
public class LoginEntity {
    private String username;
    private String password;
    private String clientId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
