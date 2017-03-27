package com.dongsw.authority.service;

import com.dongsw.authority.common.exception.IllegalDataException;
import com.dongsw.authority.common.util.StringUtils;
import com.dongsw.authority.conf.Audience;
import com.dongsw.authority.dao.ResourceDao;
import com.dongsw.authority.model.Resource;
import com.dongsw.authority.model.TokenType;
import org.apache.commons.beanutils.BeanUtils;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资源相关service
 * Created by 戚羿辰 on 2017/02/17。
 */
@Service
public class ResourceService {

    @Autowired
    private ResourceDao dao;
    @Autowired
    private Audience audience;

    /**
     * 新增,只能操作本系统数据
     *
     * @param Resource
     */
    public void insert(Resource resource) {
        dao.insert(resource, true);
    }

    /**
     * 删除,只能操作本系统数据
     *
     * @param id
     */
    public void delete(Integer id) {
        dao.deleteById(id);
    }

    /**
     * 更新,只能操作本系统数据
     *
     * @param Resource
     */
    public void update(Resource resource) {
        dao.updateById(resource);
    }

    /**
     * 查询,只能操作本系统数据
     *
     * @param id
     * @return
     */
    public Resource get(Integer id) {
        return dao.unique(id);
    }

    /**
     * 搜索,只能操作本系统数据
     *
     * @param Resource
     * @param pageNo
     * @param pageSize
     * @return
     */
    public PageQuery<Resource> query(Resource resource, Integer pageSize, Integer pageNo) {
        PageQuery<Resource> query = new PageQuery<>();
        query.setPageSize(pageSize);
        query.setPageNumber(pageNo);
        query.setParas(resource);
        dao.find(query);
        return query;
    }

    /**
     * 查找所有资源对权限
     *
     * @return
     */
    public Map<String, String> findResourcePermission() {
        Map<String, String> map = new HashMap<>();
        List<Resource> resources = dao.queryResourceDetail();
        for (Resource resource : resources) {
        	if ("anon".equals(resource.getDefaultPermission())) {
        		map.put("/send" + resource.getResourcePath(), resource.getDefaultPermission());
        	}else if (StringUtils.isBlank(resource.getPermStr())) {
                map.put("/send" + resource.getResourcePath(), resource.getDefaultPermission() + ",roles[admin]");
            }else {
                StringBuilder perms = new StringBuilder(resource.getDefaultPermission() + ",rest[");
                perms.append(resource.getPermStr());
                perms.append("]");
                map.put("/send" + resource.getResourcePath(), perms.toString());
            }
        }
        map.put("/user/login", "anon");
        return map;
    }

    /**
     * 通过资源路径查找资源详情,附带令牌
     *
     * @return
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     */
    @Cacheable(value = "redisCache",keyGenerator = "authorityKeyGenerator")
    public Resource getResourceDetail(Resource r) throws IllegalDataException {
        Resource resource = r;
        resource = dao.getResourceDetail(resource);
        if (resource != null) {
            Object o = resource.get("tokenType");
            if (o != null) {
                TokenType tokenType = new TokenType();
                try {
                    BeanUtils.copyProperties(tokenType, o);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new IllegalDataException("令牌类型拷贝发生异常",e);
                }
                resource.setTokenType(tokenType);
            }
        }
        return resource;
    }

    /**
     * 根据当前地址获取tokenType
     * @param uri
     * @return
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     */
	public TokenType getTokenType(String uri) throws IllegalDataException {
		Resource resource = new Resource();
		URI redirectUri = URI.create(uri);
		resource.setResourcePath(redirectUri.getPath());
		resource = getResourceDetail(resource);
		if (resource == null)
		{
			return null;
		}
		//拼装accessToken
		TokenType tokenType = resource.getTokenType();
		if (tokenType == null)
		{
			tokenType = new TokenType();
			tokenType.setExpire(audience.getDefaultExpire());
			tokenType.setCheckTimes(audience.getDefaultCheckTimes());
		}
		return tokenType;
	}
}
