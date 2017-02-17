package com.quheng.usercenter.repository;

import java.util.List;

import com.quheng.usercenter.bean.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer>{
	@Query(value = "select * from user limit ?1", nativeQuery =true)
	List<User> findAllUsersByCount(int count);
	@Query(value = "select * from user where name = ?1", nativeQuery =true)
	User findUserByName(String userName);
}
