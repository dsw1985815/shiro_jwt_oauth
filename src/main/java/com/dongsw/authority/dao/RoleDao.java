package com.dongsw.authority.dao;

import java.util.List;

import com.dongsw.authority.model.Permission;
import com.dongsw.authority.model.Role;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

public interface RoleDao extends BaseMapper<Role> {

	List<Role> findOtherRole(Role role);

	List<Role> findRole(Role role);

	/**
	 * 删除角色权限
	 * @param role
	 */
	void deletePermission(Role role);

	/**
	 * 添加角色权限
	 * @param permissionList
	 */
	void insertPermission(List<Permission> permissionList);

	/**
	 * 查询权限
	 * @param roleId
	 * @param platType
	 * @return
	 */
	Role selectRoleByPlat(@Param("id") Integer userId, @Param("platType") String platType);
	/**
	 * 根据平台类型和id查找用户拥有的角色
	 * @param userId
	 * @param platType
	 * @return
	 */
	List<Role> selectUserRoleByPlat(@Param("id") Integer userId, @Param("platType") String platType);

	List<Role> find(PageQuery<Role> page);
}
