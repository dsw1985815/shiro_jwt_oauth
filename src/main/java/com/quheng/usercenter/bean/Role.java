package com.quheng.usercenter.bean;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
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
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    public int getId() {
        return id;
    }

    //省略其它内容
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "roles", fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<User>();        //用户集合

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
