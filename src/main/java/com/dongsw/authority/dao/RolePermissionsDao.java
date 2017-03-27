package com.dongsw.authority.dao;

import org.beetl.sql.core.mapper.BaseMapper;

import com.dongsw.authority.model.RolePermissions;

public interface RolePermissionsDao extends BaseMapper<RolePermissions> {

	/**
	 * 删除指定角色对权限关系
	 * @param rolePermissions
	 */
	void deletePermissions(RolePermissions rolePermissions);

}
