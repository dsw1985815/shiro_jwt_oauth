package com.dongsw.authority.dao;

import com.dongsw.authority.model.*;
import org.apache.log4j.Logger;
import org.beetl.sql.core.engine.PageQuery;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
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
public class AuthorityDaoTest extends BaseDaoTestCase {
    private static Logger logger = Logger.getLogger(AuthorityDaoTest.class);


    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private PermissionResourcesDao permissionResourcesDao;
    @Autowired
    private ResourceDao resourceDao;
    @Autowired
    private ResourceFiltersDao resourceFiltersDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private RolePermissionsDao rolePermissionsDao;
    @Autowired
    private TokenTypeDao tokenTypeDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserLoginTypeDao userLoginTypeDao;
    @Autowired
    private UserRolesDao userRolesDao;

    private static AccessToken accessToken;
    private static AccessTokenCache accessTokenCache;
    private static LoginPara loginPara;
    private static Permission permission;
    private static PermissionResources permissionResources;
    private static Resource resource;
    private static ResourceFilters resourceFilters;
    private static Role role;
    private static RolePermissions rolePermissions;
    private static TokenType tokenType;
    private static User user;
    private static UserLoginType userLoginType;
    private static UserRoles userRoles;
    private static PageQuery<Permission> queryPermission;
    private static PageQuery<Resource> queryResource;
    private static PageQuery<User> queryUser;
    private static PageQuery<UserLoginType> queryUserLoginType;
    private static PageQuery<Role> queryRole;
    private static PageQuery<TokenType> queryTokenType;
    private static PageQuery<ResourceFilters> queryResourceFilters;
    private static PageQuery<PermissionResources> queryPermissionResources;


    @BeforeClass
    public static void init() {
        try {
            accessToken = initParam(AccessToken.class);
            accessTokenCache = initParam(AccessTokenCache.class);
            loginPara = initParam(LoginPara.class);
            permission = initParam(Permission.class);
            permissionResources = initParam(PermissionResources.class);
            resource = initParam(Resource.class);
            resourceFilters = initParam(ResourceFilters.class);
            role = initParam(Role.class);
            rolePermissions = initParam(RolePermissions.class);
            tokenType = initParam(TokenType.class);
            user = initParam(User.class);
            userLoginType = initParam(UserLoginType.class);
            userRoles = initParam(UserRoles.class);
            queryPermission = initParam(PageQuery.class,Permission.class);
            queryResource = initParam(PageQuery.class,Resource.class);
            queryUser = initParam(PageQuery.class,User.class);
            queryUserLoginType = initParam(PageQuery.class,UserLoginType.class);
            queryRole = initParam(PageQuery.class,Role.class);
            queryTokenType = initParam(PageQuery.class,TokenType.class);
            queryResourceFilters = initParam(PageQuery.class,ResourceFilters.class);
            queryPermissionResources = initParam(PageQuery.class,PermissionResources.class);
        } catch (Exception e) {
            logger.error("参数初始化失败，重新生成参数文件",e);
            prepareParamFile(AuthorityDaoTest.class);
        }
    }

    @Test
    public void testPermissionDao() {
        permissionDao.selectRolePermissionByPlat(permission.getId(), "test");
        permissionDao.findAll(queryPermission);
    }


    @Test
    public void testResourceDao() {
        resourceDao.find(queryResource);
        resourceDao.findResource(queryResource);
        resourceDao.getResourceDetail(resource);
        resourceDao.queryResourceDetail();
    }

    @Test
    public void testPermissionResourcesDao() {
        permissionResourcesDao.deleteResource(permissionResources);
    }

    @Test
    public void testResourceFiltersDao() {
        //没用到
    }

    @Test
    public void testRoleDao() {
        roleDao.selectRoleByPlat(user.getId(), "user");
        roleDao.find(queryRole);
        roleDao.findOtherRole(role);
        List<Permission> permissionList = new ArrayList<>();
        permissionList.add(permission);
        roleDao.selectUserRoleByPlat(user.getId(), "user");

    }

    @Test
    public void testRolePermissionsDao() {
        rolePermissionsDao.deletePermissions(rolePermissions);

    }

    @Test
    public void testTokenTypeDao() {
        //没有用到
    }


    @Test
    public void testUserDao() {
        userDao.find(queryUser);
        List<Role> roleList = new ArrayList<Role>();
        roleList.add(role);
        userDao.selectUserByName(user.getUsername(), user.getPlatType());
        userDao.find(queryUser);
    }

    @Test
    public void testUserLoginTypeDao() {
        userLoginTypeDao.get(userLoginType.getPlatType());
        userLoginTypeDao.update(userLoginType);
    }

    @Test
    public void testUserRolesDao() {
        //没用到
    }

}


