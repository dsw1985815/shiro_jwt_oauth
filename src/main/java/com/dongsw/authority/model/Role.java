package com.dongsw.authority.model;

import com.dongsw.authority.common.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
* 
* gen by beetlsql 2017-02-17
*/
@ApiModel(value = "用户角色关系", description = "戚羿辰,董双伟")
public class Role {

	@ApiModelProperty(value = "角色id", dataType = "String")
	private Integer				id;
	@ApiModelProperty(value = "角色名称", dataType = "String")
	private String				roleName;
	@ApiModelProperty(value = "平台类型", dataType = "String")
	private String				platType;
	@ApiModelProperty(value = "userCode", dataType = "String")
	private String				userCode;

	@JsonIgnoreProperties
	private List<Permission>	permissionList;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public List<Permission> getPermissionList() {
		return permissionList;
	}

	public void setPermissionList(List<Permission> permissionList) {
		this.permissionList = permissionList;
	}

	public String getPlatType() {
		return platType;
	}

	public void setPlatType(String platType) {
		this.platType = platType;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	/**
	 * 获取用户权限信息
	 * @return
	 */
	public Set<String> getStringPermissions() {
		if (null == permissionList || permissionList.isEmpty())
		{
			return null;
		}
		Set<String> permissions = new HashSet<>();
		for (Permission permission : permissionList)
		{
			// 如果存在具体的方法级权限,则拼接具体权限
			String method = StringUtils.isBlank(permission.getMethod())  ? "" : ":" + permission.getMethod();
			permissions.add(permission.getPermission() + method);
		}
		return permissions;
	}

}
