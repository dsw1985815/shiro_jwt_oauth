package com.quheng.usercenter.controller;

import com.quheng.usercenter.bean.User;
import com.quheng.usercenter.config.Audience;
import com.quheng.usercenter.repository.RoleRepository;
import com.quheng.usercenter.repository.UserRepository;
import com.quheng.usercenter.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;


@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserRepository userRepositoy;
    @Autowired
    private RoleRepository roleRepositoy;
    @Autowired
    private Audience audienceEntity;


    @RequestMapping("testpage")
    public Object testpage() {
        ModelAndView mv = new ModelAndView("user_register");
        mv.addObject("clientId", audienceEntity.getClientId());
        return mv;
    }

    @RequestMapping("login")
    public Object login(@RequestBody LoginEntity loginEntity) {
        ModelAndView mv = new ModelAndView("user_login");
        mv.addObject("loginEntity", loginEntity);
        mv.addObject("clientId", loginEntity.getClientId());
        return mv;
    }

    @RequestMapping("detail")
    public Object getUser(int id) {
        ModelAndView mv = new ModelAndView("user_detail");
        User userEntity = userRepositoy.findOne(id);
        ResultMsg resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), userEntity);
        mv.addObject("resultMsg", resultMsg);
        return mv;
    }


    @Modifying
    @RequestMapping("adduser")
    public Object addUser(@RequestBody User userEntity) {
        ModelAndView mv = new ModelAndView("user_login");
        String salt = RandomUtils.generateLowerString(6);
        userEntity.setSalt(salt);
        userEntity.setGuid(MakeUUID.getUUID());
        String md5Password = MyUtils.getMD5(userEntity.getPassword() + userEntity.getSalt());
        userEntity.setPassword(md5Password);
        userEntity.setCreateTime(new Date());
        userRepositoy.save(userEntity);
        ResultMsg resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), userEntity);
        mv.addObject("resultMsg", resultMsg);
        mv.addObject("userEntity", userEntity);
        mv.addObject("clientId", audienceEntity.getClientId());
        return mv;
    }

    @Modifying
    @RequestMapping("updateuser")
    public Object updateUser(@RequestBody User userEntity) {
        User user = userRepositoy.findOne(userEntity.getId());
        if (user != null) {
            user.setUsername(userEntity.getUsername());
            userRepositoy.save(user);
        }
        ResultMsg resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), null);
        return resultMsg;
    }

    @Modifying
    @RequestMapping("deleteuser")
    public Object deleteUser(int id) {
        userRepositoy.delete(id);
        ResultMsg resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), null);
        return resultMsg;
    }
}
