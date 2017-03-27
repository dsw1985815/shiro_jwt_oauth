package com.dongsw.authority.service;

import com.dongsw.authority.dao.PermissionDao;
import com.dongsw.authority.dao.RoleDao;
import com.dongsw.authority.dao.RolePermissionsDao;
import com.dongsw.authority.model.Permission;
import com.dongsw.authority.model.Role;
import com.dongsw.authority.model.RolePermissions;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 角色相关service
 * Created by 戚羿辰 on 2017/02/17。
 */
@Service
public class RoleService {

	@Autowired
	private RoleDao dao;
	
	@Autowired
	private PermissionDao permissionDao;
	
	@Autowired
	private RolePermissionsDao rolePermissionsDao;
	
	/**
	 * 查询,只能操作本系统数据
	 * @param id
	 * @return 
	 */
	public Role get(Integer id) {
		return dao.unique(id);
	}

	/**
	 * 搜索,只能操作本系统数据
	 * @param role
	 * @return 
	 */
	public List<Role> query(Role role) {
		// 其他系统的角色列表
		List<Role> otherList = dao.findOtherRole(role);
		return otherList;
	}

	/**
	 * 查询角色信息
	 * @param roleId
	 * @param platType
	 * @return
	 */
	public Role getRole(Integer roleId, String platType){
		Role role = dao.selectRoleByPlat(roleId, platType);
		List<Permission> permissionList = permissionDao.selectRolePermissionByPlat(roleId, platType);
		if (null != role) {
			role.setPermissionList(permissionList);
		}
		return role;
	}

	/**
	 * 分页查询权限
	 * @param roleName
	 * @param platType
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */
	public PageQuery<Role> query(String roleName, String platType, Integer pageSize, Integer pageNo) {
		Map<String, Object> map = new HashMap<>();
		map.put("roleName", roleName);
		map.put("platType", platType);
		PageQuery<Role> query = new PageQuery<>();
		query.setPageSize(pageSize);
		query.setPageNumber(pageNo);
		query.setParas(map);
		dao.find(query);
		return query;
	}

	/**
	 * 查询角色对应的权限
	 * @param perms
	 * @return
	 */
	public List<Permission> queryPermission(RolePermissions perms) {
		return permissionDao.selectRolePermissionByPlat(perms.getRoleId(), perms.getPlatType());
	}

	/**
	 * 插入权限
	 * @param rolePermissions
	 * @return
	 */
	public void insertPermission(RolePermissions rolePermissions) {
		rolePermissionsDao.insert(rolePermissions);
	}

	/**
	 * 删除权限
	 * @param rolePermissions
	 * @return
	 */
	public void delPermission(RolePermissions rolePermissions) {
		rolePermissionsDao.deletePermissions(rolePermissions);
	}
	
}
