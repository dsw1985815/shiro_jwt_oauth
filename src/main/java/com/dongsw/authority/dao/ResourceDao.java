package com.dongsw.authority.dao;

import java.util.List;

import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

import com.dongsw.authority.model.Resource;

public interface ResourceDao extends BaseMapper<Resource> {

	List<Resource> findOther(Resource resource);

	List<Resource> findAll(Resource resource);

	/**
	 * 查找资源列表(附带对应权限详情)
	 * @return
	 */
	List<Resource> queryResourceDetail();

	/**
	 * 通过资源路径查找资源详情,附带令牌
	 * @return
	 */
	Resource getResourceDetail(Resource resource);

	List<Resource> find(PageQuery<Resource> query);


    List<Resource> findResource(PageQuery<Resource> query);
}
