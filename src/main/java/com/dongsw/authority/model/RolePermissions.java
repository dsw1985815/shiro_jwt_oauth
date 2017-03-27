package com.dongsw.authority.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/*
* 
* gen by beetlsql 2017-02-17
*/
@ApiModel(value = "角色对权限", description = "戚羿辰,董双伟")
public class RolePermissions {

	@ApiModelProperty(value = "权限id", dataType = "String")
	private Integer	permissionId;
	@ApiModelProperty(value = "角色id", dataType = "String")
	private Integer	roleId;
	@ApiModelProperty(value = "平台类型", dataType = "String")
	private String	platType;
	@ApiModelProperty(value = "权限方法[read,create,update,delete],为空则为所有", dataType = "String")
	private String	method;

	public Integer getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Integer permissionId) {
		this.permissionId = permissionId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getPlatType() {
		return platType;
	}

	public void setPlatType(String platType) {
		this.platType = platType;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

}
