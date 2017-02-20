package com.quheng.usercenter.controller;

import com.quheng.usercenter.bean.Role;
import com.quheng.usercenter.bean.User;
import com.quheng.usercenter.jwt.AccessToken;
import com.quheng.usercenter.config.Audience;
import com.quheng.usercenter.jwt.JwtHelper;
import com.quheng.usercenter.jwt.LoginPara;
import com.quheng.usercenter.repository.RoleRepository;
import com.quheng.usercenter.repository.UserRepository;
import com.quheng.usercenter.utils.MyUtils;
import com.quheng.usercenter.utils.ResultMsg;
import com.quheng.usercenter.utils.ResultStatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;


@RestController
@RequestMapping("user")
public class UserController {
	@Autowired
	private UserRepository userRepositoy;
	@Autowired
	private RoleRepository roleRepositoy;
	
	@RequestMapping("getuser")
	public Object getUser(int id)
	{
		ModelAndView mv = new ModelAndView("user");
		User userEntity = userRepositoy.findOne(id);
		ResultMsg resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), userEntity);
		mv.addObject("resultMsg",resultMsg);
		return mv;
	}
	
	@RequestMapping("getusers")
	public Object getUsers(String role)
	{
		//List<User> userEntities = userRepositoy.findUserByRole(role);
		ResultMsg resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), new ArrayList<User>());
		return "users/user_plus";

		//return resultMsg;
	}

	@RequestMapping("login")
	public Object login(String role)
	{
		ModelAndView mv = new ModelAndView("user_login");
		return mv;

		//return resultMsg;
	}
	
	@Modifying
	@RequestMapping("adduser")
	public Object addUser(@RequestBody User userEntity)
	{
		userRepositoy.save(userEntity);
		ResultMsg resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), userEntity);
		return resultMsg;
	}
	
	@Modifying
	@RequestMapping("updateuser")
	public Object updateUser(@RequestBody User userEntity)
	{
		User user = userRepositoy.findOne(userEntity.getId());
		if (user != null)
		{
			user.setUsername(userEntity.getUsername());
			userRepositoy.save(user);
		}
		ResultMsg resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), null);
		return resultMsg;
	}
	
	@Modifying
	@RequestMapping("deleteuser")
	public Object deleteUser(int id)
	{
		userRepositoy.delete(id);
		ResultMsg resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), null);
		return resultMsg;
	}
}
