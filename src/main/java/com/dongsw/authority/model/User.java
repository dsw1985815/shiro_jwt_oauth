package com.dongsw.authority.model;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/*
* 
* gen by beetlsql 2017-02-17
*/
@ApiModel(value = "用户", description = "戚羿辰,董双伟")
public class User {

	@ApiModelProperty(value = "id", dataType = "String")
	private Integer	id;
	@ApiModelProperty(value = "密码", dataType = "String")
	private String	password;
	@ApiModelProperty(value = "手机号", dataType = "String")
	private String	phone;
	@ApiModelProperty(value = "用户名", dataType = "String")
	private String	username;
	@ApiModelProperty(value = "slat", dataType = "String")
	private String salt;
	@ApiModelProperty(value = "userCode", dataType = "String")
	private String userCode;
	@ApiModelProperty(value = "用户登录平台(0web 1ios 2安卓)", dataType = "String")
	private Integer userPlat;
	/**
	 * 平台类型
	 */
	@ApiModelProperty(value = "平台类型", dataType = "String")
	private String	platType;

	@ApiModelProperty(hidden = true)
	private boolean isOnline;
	
	List<Role>		roles;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public String getPlatType() {
		return platType;
	}

	public void setPlatType(String platType) {
		this.platType = platType;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public Integer getUserPlat() {
		return userPlat;
	}

	public void setUserPlat(Integer userPlat) {
		this.userPlat = userPlat;
	}

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}
	
}
