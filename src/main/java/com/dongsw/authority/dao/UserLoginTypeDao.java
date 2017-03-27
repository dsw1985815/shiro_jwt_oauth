package com.dongsw.authority.dao;

import java.util.List;

import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.mapper.BaseMapper;

import com.dongsw.authority.model.UserLoginType;

public interface UserLoginTypeDao extends BaseMapper<UserLoginType> {

	List<UserLoginType> get(@Param("platType") Integer platType);

	UserLoginType update(UserLoginType userLoginType);

}
