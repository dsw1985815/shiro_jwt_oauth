package com.dongsw.authority.service;

import com.dongsw.authority.dao.PermissionDao;
import com.dongsw.authority.dao.RoleDao;
import com.dongsw.authority.dao.RolePermissionsDao;
import com.dongsw.authority.model.Permission;
import com.dongsw.authority.model.Role;
import com.dongsw.authority.model.RolePermissions;
import org.beetl.sql.core.engine.PageQuery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

/**
 * Comments：
 * Author：dongshuangwei
 * Create Date：2017/3/22
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version：v1.0
 */
public class RoleServiceTest {
    @Mock
    RoleDao dao;
    @Mock
    PermissionDao permissionDao;
    @Mock
    RolePermissionsDao rolePermissionsDao;
    @InjectMocks
    RoleService roleService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGet(){
        Role result = roleService.get(Integer.valueOf(0));
        Assert.assertEquals(new Role(), result);
    }

    @Test
    public void testQuery(){
        List<Role> result = roleService.query(new Role());
        Assert.assertEquals(Arrays.<Role>asList(new Role()), result);
    }

    @Test
    public void testGetRole(){
        Role result = roleService.getRole(Integer.valueOf(0), "platType");
        Assert.assertEquals(new Role(), result);
    }

    @Test
    public void testQuery2(){
        PageQuery<Role> result = roleService.query("roleName", "platType", Integer.valueOf(0), Integer.valueOf(0));
        Assert.assertEquals(new PageQuery<Role>(0L, new Object(), "userDefinedOrderBy", 0L), result);
    }

    @Test
    public void testQueryPermission(){
        List<Permission> result = roleService.queryPermission(new RolePermissions());
        Assert.assertEquals(Arrays.<Permission>asList(new Permission("permission", "permissionCode")), result);
    }

    @Test
    public void testInsertPermission(){
        roleService.insertPermission(new RolePermissions());
    }

    @Test
    public void testDelPermission(){
        roleService.delPermission(new RolePermissions());
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme