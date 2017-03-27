package com.dongsw.authority.service;

import com.dongsw.authority.dao.TokenTypeDao;
import com.dongsw.authority.model.TokenType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Comments：
 * Author：dongshuangwei
 * Create Date：2017/2/22
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version：v1.0
 */
@Service
public class TokenTypeService {

    @Autowired
    private TokenTypeDao dao;

    /**
     * 新增,只能操作本系统数据
     *
     * @param tokenType
     */
    public void insert(TokenType tokenType) {
        dao.insert(tokenType, true);
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
     * @param tokenType
     */
    public void update(TokenType tokenType) {
        dao.updateById(tokenType);
    }

    /**
     * 查询,只能操作本系统数据
     *
     * @param id
     * @return
     */
    public TokenType get(Integer id) {
        return dao.unique(id);
    }

	public Object all() {
		return dao.all();
	}

}
