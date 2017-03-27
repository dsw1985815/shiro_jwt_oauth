package com.dongsw.authority.service;

import com.dongsw.authority.dao.RolePermissionsDao;
import com.dongsw.authority.model.RolePermissions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Comments：
 * Author：dongshuangwei
 * Create Date：2017/3/22
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version：v1.0
 */
public class RolePermissionsServiceTest {
    @Mock
    RolePermissionsDao dao;
    @InjectMocks
    RolePermissionsService rolePermissionsService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInsert() {
        rolePermissionsService.insert(new RolePermissions());
    }

    @Test
    public void testDelete() {
        rolePermissionsService.delete(Integer.valueOf(0));
    }

    @Test
    public void testUpdate() {
        rolePermissionsService.update(new RolePermissions());
    }

    @Test
    public void testGet() {
        RolePermissions result = rolePermissionsService.get(Integer.valueOf(0));
        Assert.assertEquals(new RolePermissions(), result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme