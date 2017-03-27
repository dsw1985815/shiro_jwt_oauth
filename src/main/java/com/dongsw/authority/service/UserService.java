package com.dongsw.authority.service;

import com.dongsw.authority.common.def.ConstantDef;
import com.dongsw.authority.dao.PermissionDao;
import com.dongsw.authority.dao.RoleDao;
import com.dongsw.authority.dao.UserDao;
import com.dongsw.authority.dao.UserLoginTypeDao;
import com.dongsw.authority.model.Permission;
import com.dongsw.authority.model.Role;
import com.dongsw.authority.model.User;
import com.dongsw.authority.model.UserLoginType;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用户相关service
 * Created by 戚羿辰 on 2017/02/17。
 */
@Service
@SuppressWarnings("unchecked")
public class UserService {

    @Autowired
    private UserDao dao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private UserLoginTypeDao userLoginTypeDao;
    @Autowired
    private RedisTemplate<Serializable, Object> redisTemplate;

    /**
     * 新增,只能操作本系统数据
     *
     * @param User
     */
    public void insert(User user) {
        dao.insert(user, true);
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
     * @param User
     */
    public void update(User user) {
        dao.updateById(user);
    }

    /**
     * 查询,只能操作本系统数据
     *
     * @param id
     * @return
     */
    public User get(Integer id) {
        return dao.unique(id);
    }

    public void bindRole(User user) {
        dao.deleteRoles(user);
        dao.insertRoles(user.getRoles());
    }

    /**
     * 根据用户id和平台类型获取用户
     *
     * @param userId
     * @param platType
     * @return
     */
    public User getUser(Integer userId, String platType) {
        User user = dao.selectUserByPlat(userId, platType);
        List<Role> roleList = roleDao.selectUserRoleByPlat(userId, platType);
        user.setRoles(roleList);
        return user;
    }

    /**
     * 根据用户名和平台类型获取用户
     *
     * @param userName
     * @param platformId
     * @return User
     */
    public User getUserByName(String userName, String platformId) {
        User user = dao.selectUserByName(userName, platformId);
        if (user != null) {
            List<Role> roleList = roleDao.selectUserRoleByPlat(user.getId(), platformId);
            if (roleList != null) {
                for (Role role : roleList) {
                    List<Permission> permission = permissionDao.selectRolePermissionByPlat(role.getId(), platformId);
                    role.setPermissionList(permission);
                }
                user.setRoles(roleList);
            }
        }
        return user;
    }


    public PageQuery<User> query(String username, String platType, Integer pageSize, Integer pageNo) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("platType", platType);
        PageQuery<User> query = new PageQuery<>();
        query.setPageSize(pageSize);
        query.setPageNumber(pageNo);
        query.setParas(map);
        dao.find(query);
        for (User user : query.getList()) {
            String key = ConstantDef.ONLINE_USER_SET + user.getUsername() + ConstantDef.SPLIT_COMA + user.getPlatType();
            Map<String, List<String>> userMap = (Map<String, List<String>>) redisTemplate.opsForValue().get(key);
            if (null != userMap && !userMap.isEmpty()) {
                user.setOnline(true);
            }
        }
        return query;
    }

    public List<UserLoginType> getLoginType() {
        return userLoginTypeDao.all();
    }

    public UserLoginType updateLoginType(UserLoginType userLoginType) {
        return userLoginTypeDao.update(userLoginType);
    }


    public SimpleAuthorizationInfo getSimpleAuthorizationInfoByShiroRealm(String loginInfo) {
        User user = getUser(loginInfo);
        if (user != null && null != user.getRoles()) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            for (Role role : user.getRoles()) {
                info.addRole(role.getRoleName());
                info.setStringPermissions(role.getStringPermissions());
            }
            return info;
        }
        return null;
    }

    public SimpleAuthorizationInfo getSimpleAuthorizationInfoByStatelessRealm(String loginInfo) {
        User user = getUser(loginInfo);
        if (user != null && null != user.getRoles()) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            for (Role role : user.getRoles()) {
                info.addRole(role.getRoleName());
                Set<String> permissions = role.getStringPermissions();
                if (null != permissions) {
                    info.addStringPermissions(permissions);
                }
            }
            return info;
        }
        return null;
    }

    private User getUser(String loginInfo) {

        String[] loginInfoArr = loginInfo.split(ConstantDef.SPLIT_COMA);
        if (loginInfoArr.length < 2) {
            return null;
        }
        // 用户名
        String userName = loginInfoArr[0];
        // 用户平台
        String platType = loginInfoArr[1];

        return getUserByName(userName, platType);
    }

}
