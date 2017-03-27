package com.dongsw.authority.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/*
* 
* gen by beetlsql 2017-02-17
*/
@ApiModel(value = "权限", description = "戚羿辰,董双伟")
public class Permission {

	@ApiModelProperty(value = "id", dataType = "Integer")
	private Integer	id;
	//权限内容
	@ApiModelProperty(value = "权限", dataType = "String")
	private String	permission;
	//唯一标识权限的字符串
	@ApiModelProperty(value = "权限编码", dataType = "String")
	private String	permissionCode;
	// 可以执行的方法,不设为所有,具体为[read,create,update,delete]
	@ApiModelProperty(value = "可以执行的方法,不设为所有,具体为[read,create,update,delete]", dataType = "String")
	private String	method;

	public Permission()
	{
	}

	public Permission(String permission, String permissionCode)
	{
		this.permission = permission;
		this.permissionCode = permissionCode;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getPermissionCode() {
		return permissionCode;
	}

	public void setPermissionCode(String permissionCode) {
		this.permissionCode = permissionCode;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

}
