package com.dongsw.authority.dao;

import com.dongsw.authority.model.Permission;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

import java.util.List;

public interface PermissionDao extends BaseMapper<Permission> {


	List<Permission> findAll(PageQuery<Permission> query);

	/**
	 * 查询角色和权限关系
	 * @param userId
	 * @param platType
	 * @return List<Permission>
	 */
	List<Permission> selectRolePermissionByPlat(@Param("id") Integer userId, @Param("platType") String platType);

}
