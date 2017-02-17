package com.quheng.usercenter.bean;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Comments：
 * Author：dongshuangwei
 * Create Date：2017/2/17
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version：v1.0
 */

@Entity
@Table(name="role")
public class Role {

    private Set<User> users = new HashSet<User>();

    @JoinTable(name = "user_roles", joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "userId")}, inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "roleId")})
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JsonIgnore
    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
