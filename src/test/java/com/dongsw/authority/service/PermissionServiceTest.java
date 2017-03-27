package com.dongsw.authority.service;

import com.dongsw.authority.common.exception.IllegalDataException;
import com.dongsw.authority.dao.AuthorityDaoTest;
import com.dongsw.authority.dao.PermissionDao;
import com.dongsw.authority.dao.PermissionResourcesDao;
import com.dongsw.authority.dao.ResourceDao;
import com.dongsw.authority.model.Permission;
import com.dongsw.authority.model.PermissionResources;
import com.dongsw.authority.model.Resource;
import com.dongsw.authority.shiro.MShiroFilterFactoryBean;
import org.apache.log4j.Logger;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.NamedFilterList;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.beetl.sql.core.engine.PageQuery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
public class PermissionServiceTest extends BaseServiceTestCase {

    private static Logger logger = Logger.getLogger(AuthorityDaoTest.class);

    @Mock
    PermissionDao permissionDao;
    @Mock
    ResourceService resourceService;
    @Mock
    ResourceDao resourceDao;
    @Mock
    PermissionResourcesDao permissionResourcesDao;
    @Mock
    MShiroFilterFactoryBean shiroFilterFactoryBean;
    @Mock
    AbstractShiroFilter abstractShiroFilter;
    @Mock
    PathMatchingFilterChainResolver filterChainResolver;
    @Mock
    DefaultFilterChainManager defaultFilterChainManager;
    @Mock
    Map<String, NamedFilterList> map;
    @Mock
    private Map<String, String> map2;
    @InjectMocks
    PermissionService permissionService;

    private static Permission permission;
    private static PermissionResources permissionResources;
    private static Resource resource;


    @BeforeClass
    public static void init() {
        try {
            permission = initParam(Permission.class);
            permissionResources = initParam(PermissionResources.class);
            resource = initParam(Resource.class);
        } catch (Exception e) {
            logger.error("参数初始化失败，重新生成参数文件", e);
            prepareParamFile(PermissionServiceTest.class);
        }
    }


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInsert() {
        doNothing().when(permissionDao).insert(permission, true);
        permissionService.insert(permission);
        verify(permissionDao, times(1)).insert(permission, true);
    }

    @Test
    public void testDelete() {
        doReturn(1).when(permissionDao).deleteById(permission.getId());
        permissionService.delete(permission.getId());
        verify(permissionDao, times(1)).deleteById(permission.getId());
    }

    @Test
    public void testUpdate() {
        doReturn(1).when(permissionDao).updateById(permission);
        permissionService.update(permission);
        verify(permissionDao, times(1)).updateById(permission);
    }

    @Test
    public void testGet() {
        doReturn(permission).when(permissionDao).unique(permission.getId());
        Permission result = permissionService.get(permission.getId());
        verify(permissionDao, times(1)).unique(permission.getId());
        Assert.assertEquals(permission, result);
    }

    @Test
    public void testQuery() {
        List<Permission> permissionList = new ArrayList<>();
        permissionList.add(permission);
        doReturn(permissionList).when(permissionDao).findAll((PageQuery<Permission>) any());
        PageQuery<Permission> result = permissionService.query(permission, 10, 1);
        verify(permissionDao, times(1)).findAll((PageQuery<Permission>) any());

    }

    @Test
    public void testGetPermission() {
        doReturn(permission).when(permissionDao).unique(permission.getId());
        Permission result = permissionService.getPermission(permission.getId());
        verify(permissionDao, times(1)).unique(permission.getId());
    }

    @Test
    public void testInsertResource() {
        try {
            mockMethod();
            doNothing().when(permissionResourcesDao).insert(permissionResources);
            permissionService.insertResource(permissionResources);
        } catch (IllegalDataException e) {
            Assert.fail("测试发生异常:" + e.getMessage());
        }
    }

    @Test
    public void testDelResource() {
        try {

            mockMethod();
            doNothing().when(permissionResourcesDao).deleteResource(permissionResources);
            permissionService.delResource(permissionResources);
        } catch (IllegalDataException e) {

        }
    }

    @Test
    public void testQueryPermission() {
        List<Resource> resourceList = new ArrayList<>();
        resourceList.add(resource);
        doReturn(resourceList).when(resourceDao).findResource((PageQuery<Resource>) any());
        PageQuery<Resource> result = permissionService.queryPermission(permissionResources, 10, 1);
        verify(resourceDao, times(1)).findResource((PageQuery<Resource>) any());
    }

    @Test
    public void testUpdatePermission() {
        mockMethod();
        try {
            permissionService.updatePermission();
        } catch (IllegalDataException e) {
            logger.error("测试发生异常", e);
            Assert.fail(e.getMessage());
        }
    }


    private void mockMethod() {
        try {
            doReturn(abstractShiroFilter).when(shiroFilterFactoryBean).getObject();
            doReturn(filterChainResolver).when(abstractShiroFilter).getFilterChainResolver();
            doReturn(defaultFilterChainManager).when(filterChainResolver).getFilterChainManager();
            doReturn(map).when(defaultFilterChainManager).getFilterChains();
            doReturn(map2).when(shiroFilterFactoryBean).getFilterChainDefinitionMap();
            doNothing().when(shiroFilterFactoryBean).setFilterChainDefinitionMap(anyMap());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme