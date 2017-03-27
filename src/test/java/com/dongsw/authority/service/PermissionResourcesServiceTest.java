package com.dongsw.authority.service;

import com.dongsw.authority.BaseTestCase;
import com.dongsw.authority.dao.PermissionResourcesDao;
import com.dongsw.authority.model.PermissionResources;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

/**
 * Comments：
 * Author：dongshuangwei
 * Create Date：2017/3/22
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version：v1.0
 */
public class PermissionResourcesServiceTest extends BaseTestCase {
    @Mock
    PermissionResourcesDao dao;
    @InjectMocks
    PermissionResourcesService permissionResourcesService;

    private PermissionResources permissionResources;

    @Before
    public void setUp() {


    }

    @Test
    public void testInsert() {
        doNothing().when(dao).insert(permissionResources, true);
        permissionResourcesService.insert(permissionResources);
        verify(dao, times(1)).insert(permissionResources, true);
    }

    @Test
    public void testDelete() {
        doReturn(1).when(dao).deleteById(permissionResources.getPermissionId());
        permissionResourcesService.delete(permissionResources.getPermissionId());
        verify(dao, times(1)).deleteById(permissionResources.getPermissionId());
    }

    @Test
    public void testUpdate() {
        doReturn(1).when(dao).updateById(permissionResources);
        permissionResourcesService.update(permissionResources);
        verify(dao, times(1)).updateById(permissionResources);
    }

    @Test
    public void testGet() {
        doReturn(permissionResources).when(dao).unique(permissionResources.getPermissionId());
        PermissionResources result = permissionResourcesService.get(permissionResources.getPermissionId());
        verify(dao, times(1)).unique(permissionResources.getPermissionId());
        Assert.assertEquals(permissionResources, result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme