package com.dongsw.authority.service;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.dongsw.authority.common.def.ConstantDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.dongsw.authority.dao.ResourceFiltersDao;
import com.dongsw.authority.model.ResourceFilters;

/**
 * 
 * 资源相关service
 * Created by 戚羿辰 on 2017/02/17。
 */
@Service
public class ResourceFiltersService {

	@Autowired
	private ResourceFiltersDao					dao;
	@Autowired
	private RedisTemplate<Serializable, Object>	template;

	/**
	 * 新增,只能操作本系统数据
	 * @param ResourceFilters
	 */
	public void insert(ResourceFilters resourceFilters) {
		dao.insert(resourceFilters, true);
		resetFilters();
	}

	/**
	 * 删除,只能操作本系统数据
	 * @param id
	 */
	public void delete(Integer id) {
		dao.deleteById(id);
		resetFilters();
	}

	/**
	 * 更新,只能操作本系统数据
	 * @param ResourceFilters
	 */
	public void update(ResourceFilters resourceFilters) {
		dao.updateById(resourceFilters);
		resetFilters();
	}

	/**
	 * 查询,只能操作本系统数据
	 * @param id
	 * @return 
	 */
	public ResourceFilters get(Integer id) {
		return dao.unique(id);
	}

	/**
	 * 查询过滤内容
	 * @return
	 */
	public List<ResourceFilters> all() {
		return dao.all();
	}

	/**
	 * 更新拦截器信息
	 */
	private void resetFilters() {
		List<ResourceFilters> list = all();
		if (null == list)
		{
			return;
		}
		Set<String> ignoreExt = new HashSet<>();
		for (ResourceFilters resource : list)
		{
			ignoreExt.add(resource.getFilterType());
		}
		template.opsForValue().set(ConstantDef.RESOURCE_FILTER_SET, ignoreExt);
	}
}
