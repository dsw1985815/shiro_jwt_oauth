package com.dongsw.authority.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "用户登录类型(多点)", description = "戚羿辰")
public class UserLoginType {

	@ApiModelProperty(value = "平台类型(0控制台 1劳务 2商城)", dataType = "Integer", required = true)
	private Integer	platType;
	@ApiModelProperty(value = "登录类型 (0单点登录,1多点登录)", dataType = "Integer", required = true)
	private Integer	loginType;
	@ApiModelProperty(value = "登录平台(0.web 1.ios 2.app)", dataType = "Integer", required = true)
	private Integer	loginPlat;
	@ApiModelProperty(value = "用户角色id", dataType = "Integer", required = true)
	private Integer	userRole;

	public Integer getPlatType() {
		return platType;
	}

	public void setPlatType(Integer platType) {
		this.platType = platType;
	}

	public Integer getLoginType() {
		return loginType;
	}

	public void setLoginType(Integer loginType) {
		this.loginType = loginType;
	}

	public Integer getLoginPlat() {
		return loginPlat;
	}

	public void setLoginPlat(Integer loginPlat) {
		this.loginPlat = loginPlat;
	}

	public Integer getUserRole() {
		return userRole;
	}

	public void setUserRole(Integer userRole) {
		this.userRole = userRole;
	}

}
