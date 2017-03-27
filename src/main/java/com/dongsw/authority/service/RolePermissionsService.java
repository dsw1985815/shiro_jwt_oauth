package com.dongsw.authority.service;

import com.dongsw.authority.dao.RolePermissionsDao;
import com.dongsw.authority.model.RolePermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class RolePermissionsService {

    @Autowired
    private RolePermissionsDao dao;

    /**
     * 新增,只能操作本系统数据
     *
     * @param rolePermissions
     */
    public void insert(RolePermissions rolePermissions) {
        dao.insert(rolePermissions, true);
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
     * @param rolePermissions
     */
    public void update(RolePermissions rolePermissions) {
        dao.updateById(rolePermissions);
    }

    /**
     * 查询,只能操作本系统数据
     *
     * @param id
     * @return
     */
    public RolePermissions get(Integer id) {
        return dao.unique(id);
    }

}
