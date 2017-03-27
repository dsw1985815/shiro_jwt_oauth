package com.dongsw.authority.dao;

import java.util.List;

import com.dongsw.authority.model.Role;
import com.dongsw.authority.model.User;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

public interface UserDao extends BaseMapper<User> {

	List<User> find(PageQuery<User> page);

	/**
	 * 删除用户角色
	 * @param user
	 */
	void deleteRoles(User user);

	/**
	 * 添加用户角色
	 * @param roles
	 */
	void insertRoles(List<Role> roles);

	/**
	 * 根据平台类型和id查找用户
	 * @param userId
	 * @param platType
	 */
	User selectUserByPlat(@Param("id") Integer userId, @Param("platType") String platType);

	User selectUserByName(@Param("userName") String userName, @Param("platType") String platType);
}
