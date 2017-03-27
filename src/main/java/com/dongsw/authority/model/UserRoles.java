package com.dongsw.authority.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/*
* 
* gen by beetlsql 2017-02-17
*/
@ApiModel(value = "用户角色关系", description = "戚羿辰,董双伟")
public class UserRoles {

	@ApiModelProperty(value = "角色id", dataType = "String")
	private Integer	roleId;
	@ApiModelProperty(value = "用哪个华id", dataType = "String")
	private Integer	userId;
	@ApiModelProperty(value = "平台类型", dataType = "String")
	private String	platType;

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getPlatType() {
		return platType;
	}

	public void setPlatType(String platType) {
		this.platType = platType;
	}

}
