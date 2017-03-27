/**
 * Comments：
 * Author：dongshuangwei
 * Create Date：2017/2/16
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version：v1.0
 */
package com.dongsw.authority.model;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "登录信息", description = "戚羿辰,董双伟")
public class LoginPara {

	private String	clientId;
	private String	userName;
	private String	password;
	private String	platformId;
	private String	redirectUri;
	private Integer	userPlat;
	private String base64Security;
	private Integer userId;
	private String userCode;
	private Integer checkTimes;
	private Integer required;
	private Integer loginPlat;
	private long ttlmillis;
	private Integer roleId;
	private Integer needVerify;
	
	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

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


	public String getRedirectUri() {
		return redirectUri;
	}

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	public String getPlatformId() {
		return platformId;
	}

	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}

	public Integer getUserPlat() {
		return userPlat;
	}

	public void setUserPlat(Integer userPlat) {
		this.userPlat = userPlat;
	}

	public String getBase64Security() {
		return base64Security;
	}

	public void setBase64Security(String base64Security) {
		this.base64Security = base64Security;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getCheckTimes() {
		return checkTimes;
	}

	public void setCheckTimes(Integer checkTimes) {
		this.checkTimes = checkTimes;
	}

	public Integer getRequired() {
		return required;
	}

	public void setRequired(Integer required) {
		this.required = required;
	}

	public Integer getLoginPlat() {
		return loginPlat;
	}

	public void setLoginPlat(Integer loginPlat) {
		this.loginPlat = loginPlat;
	}

	public long getTtlmillis() {
		return ttlmillis;
	}

	public void setTtlmillis(long ttlmillis) {
		this.ttlmillis = ttlmillis;
	}

	public Integer getNeedVerify() {
		return needVerify;
	}

	public void setNeedVerify(Integer needVerify) {
		this.needVerify = needVerify;
	}

}