package com.dongsw.authority.service;

import com.dongsw.authority.dao.PermissionDao;
import com.dongsw.authority.dao.RoleDao;
import com.dongsw.authority.dao.UserDao;
import com.dongsw.authority.dao.UserLoginTypeDao;
import com.dongsw.authority.model.User;
import com.dongsw.authority.model.UserLoginType;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.beetl.sql.core.engine.PageQuery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Comments：
 * Author：dongshuangwei
 * Create Date：2017/3/22
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version：v1.0
 */
public class UserServiceTest {
    @Mock
    UserDao dao;
    @Mock
    RoleDao roleDao;
    @Mock
    PermissionDao permissionDao;
    @Mock
    UserLoginTypeDao userLoginTypeDao;
    @Mock
    RedisTemplate<Serializable, Object> redisTemplate;
    @InjectMocks
    UserService userService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInsert() {
        userService.insert(new User());
    }

    @Test
    public void testDelete() {
        userService.delete(Integer.valueOf(0));
    }

    @Test
    public void testUpdate() {
        userService.update(new User());
    }

    @Test
    public void testGet() {
        User result = userService.get(Integer.valueOf(0));
        Assert.assertEquals(new User(), result);
    }

    @Test
    public void testBindRole() {
        userService.bindRole(new User());
    }

    @Test
    public void testGetUser() {
        User result = userService.getUser(Integer.valueOf(0), "platType");
        Assert.assertEquals(new User(), result);
    }

    @Test
    public void testGetUserByName() {
        User result = userService.getUserByName("userName", "platformId");
        Assert.assertEquals(new User(), result);
    }

    @Test
    public void testQuery() {
        PageQuery<User> result = userService.query("username", "platType", Integer.valueOf(0), Integer.valueOf(0));
        Assert.assertEquals(new PageQuery<User>(0L, new Object(), "userDefinedOrderBy", 0L), result);
    }

    @Test
    public void testUpdateLoginType() {
        UserLoginType result = userService.updateLoginType(new UserLoginType());
        Assert.assertEquals(new UserLoginType(), result);
    }

    @Test
    public void testGetSimpleAuthorizationInfoByShiroRealm() {
        SimpleAuthorizationInfo result = userService.getSimpleAuthorizationInfoByShiroRealm("loginInfo");
        Assert.assertEquals(new SimpleAuthorizationInfo(new HashSet<String>(Arrays.asList("String"))), result);
    }

    @Test
    public void testGetSimpleAuthorizationInfoByStatelessRealm() {
        SimpleAuthorizationInfo result = userService.getSimpleAuthorizationInfoByStatelessRealm("loginInfo");
        Assert.assertEquals(new SimpleAuthorizationInfo(new HashSet<String>(Arrays.asList("String"))), result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme