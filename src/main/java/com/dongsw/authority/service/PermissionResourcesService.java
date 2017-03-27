package com.dongsw.authority.service;

import com.dongsw.authority.model.PermissionResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dongsw.authority.dao.PermissionResourcesDao;

/**
 * Comments：
 * Author：dongshuangwei
 * Create Date：2017/2/22
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version：v1.0
 */
@Service
public class PermissionResourcesService {


    @Autowired
    private PermissionResourcesDao dao;

    /**
     * 新增,只能操作本系统数据
     * @param permission
     */
    public void insert(PermissionResources permission) {
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
    public void update(PermissionResources permission) {
        dao.updateById(permission);
    }

    /**
     * 查询,只能操作本系统数据
     * @param id
     * @return
     */
    public PermissionResources get(Integer id) {
        return dao.unique(id);
    }
}
