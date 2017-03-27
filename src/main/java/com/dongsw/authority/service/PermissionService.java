
package com.dongsw.authority.service;

import com.dongsw.authority.common.exception.IllegalDataException;
import com.dongsw.authority.dao.PermissionDao;
import com.dongsw.authority.dao.PermissionResourcesDao;
import com.dongsw.authority.dao.ResourceDao;
import com.dongsw.authority.model.Permission;
import com.dongsw.authority.model.PermissionResources;
import com.dongsw.authority.model.Resource;
import com.dongsw.authority.shiro.MShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 权限相关service
 * Created by 戚羿辰 on 2017/02/17。
 */
@Service
public class PermissionService {

	@Autowired
	private PermissionDao dao;
	@Autowired
	private ResourceService	resourceService;
	@Autowired
	private ResourceDao resourceDao;
	@Autowired
	private PermissionResourcesDao permissionResourcesDao;
	@Autowired
	MShiroFilterFactoryBean shiroFilterFactoryBean;
	
	/**
	 * 新增,只能操作本系统数据
	 * @param permission
	 */
	public void insert(Permission permission) {
		dao.insert(permission, true);
	}

	/**
	 * 删除,只能操作本系统数据
	 * @param id
	 */
	public void delete(Integer id) {
		dao.deleteById(id);
	}

	/**
	 * 更新,只能操作本系统数据
	 * @param permission
	 */
	public void update(Permission permission) {
		dao.updateById(permission);
	}

	/**
	 * 查询,只能操作本系统数据
	 * @param id
	 * @return 
	 */
	public Permission get(Integer id) {
		return dao.unique(id);
	}

	/**
	 * 搜索,只能操作本系统数据
	 * @param permission
	 * @return 
	 */
	public PageQuery<Permission> query(Permission permission, Integer pageSize, Integer pageNo) {
		Map<String, Object> map = new HashMap<>();
		map.put("permission", permission.getPermission());
		map.put("permissionCode", permission.getPermissionCode());
		PageQuery<Permission> query = new PageQuery<>();
		query.setPageSize(pageSize);
		query.setPageNumber(pageNo);
		query.setParas(map);
		dao.findAll(query);
		return query;
	}

	public Permission getPermission(Integer permissionId) {
		return dao.unique(permissionId);
	}

	/**
	 * 添加权限对应的资源
	 * @param permissionResources
	 */
	public void insertResource(PermissionResources permissionResources) throws IllegalDataException {
		permissionResourcesDao.insert(permissionResources);
		shiroFilterFactoryBean.setFilterChainDefinitionMap(resourceService.findResourcePermission());
		updatePermission();
	}

	/**
	 * 删除权限对应的资源
	 * @param permissionResources
	 */
	public void delResource(PermissionResources permissionResources) throws IllegalDataException {
		permissionResourcesDao.deleteResource(permissionResources);
		shiroFilterFactoryBean.setFilterChainDefinitionMap(resourceService.findResourcePermission());
		updatePermission();
	}

	public PageQuery<Resource> queryPermission(PermissionResources permissionResources, Integer pageSize, Integer pageNo) {
		PageQuery<Resource> query = new PageQuery<>();
		query.setPageSize(pageSize);
		query.setPageNumber(pageNo);
		query.setParas(permissionResources);
		resourceDao.findResource(query);
		return query;
	}

	/**
	 * 重新加载权限
	 */
	public void updatePermission() throws IllegalDataException {

		synchronized (shiroFilterFactoryBean) {

			AbstractShiroFilter shiroFilter = null;
			try {
				shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean
						.getObject();
			} catch (Exception e) {
				throw new IllegalDataException ("get ShiroFilter from shiroFilterFactoryBean error!",e);
			}

			PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter
					.getFilterChainResolver();
			DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver
					.getFilterChainManager();

			// 清空老的权限控制
			manager.getFilterChains().clear();

			shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
			shiroFilterFactoryBean
					.setFilterChainDefinitionMap(resourceService.findResourcePermission());
			// 重新构建生成
			Map<String, String> chains = shiroFilterFactoryBean
					.getFilterChainDefinitionMap();
			for (Map.Entry<String, String> entry : chains.entrySet()) {
				String url = entry.getKey();
				String chainDefinition = entry.getValue().trim()
						.replace(" ", "");
				manager.createChain(url, chainDefinition);
			}

		}
	}
}
