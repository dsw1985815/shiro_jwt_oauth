package com.dongsw.authority.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/*
* 
* gen by beetlsql 2017-02-17
*/
@ApiModel(value = "权限资源关系", description = "戚羿辰,董双伟")
public class PermissionResources {

	@ApiModelProperty(value = "权限id", dataType = "Integer")
	private Integer	permissionId;
	@ApiModelProperty(value = "资源", dataType = "Integer")
	private String	resource;

	public Integer getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Integer permissionId) {
		this.permissionId = permissionId;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

}
