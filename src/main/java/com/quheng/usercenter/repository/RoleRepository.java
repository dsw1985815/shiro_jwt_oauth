package com.quheng.usercenter.repository;

import com.quheng.usercenter.bean.Role;
import com.quheng.usercenter.bean.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoleRepository extends CrudRepository<Role, Integer>{
}
