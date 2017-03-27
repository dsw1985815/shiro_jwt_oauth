package com.dongsw.authority.dao;

import com.dongsw.authority.model.PermissionResources;
import org.beetl.sql.core.mapper.BaseMapper;

public interface PermissionResourcesDao extends BaseMapper<PermissionResources> {

	/**
	 * 删除权限对应的资源
	 * @param permissionResources
	 */
	void deleteResource(PermissionResources permissionResources);


}
