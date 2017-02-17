package com.quheng.usercenter.controller;

import com.quheng.usercenter.bean.User;
import com.quheng.usercenter.repository.UserRepository;
import com.quheng.usercenter.utils.ResultMsg;
import com.quheng.usercenter.utils.ResultStatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;



@RestController
@RequestMapping("user")
public class UserController {
	@Autowired
	private UserRepository userRepositoy;
	
	@RequestMapping("getuser")
	public Object getUser(int id)
	{
		User userEntity = userRepositoy.findOne(id);
		ResultMsg resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), userEntity);
		return resultMsg;
	}
	
	@RequestMapping("getusers")
	public Object getUsers(String role)
	{
		//List<User> userEntities = userRepositoy.findUserByRole(role);
		ResultMsg resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), new ArrayList<User>());
		return resultMsg;
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
