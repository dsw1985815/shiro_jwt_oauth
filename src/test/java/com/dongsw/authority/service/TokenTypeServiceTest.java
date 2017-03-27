package com.dongsw.authority.service;

import com.dongsw.authority.dao.TokenTypeDao;
import com.dongsw.authority.model.TokenType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Comments：
 * Author：dongshuangwei
 * Create Date：2017/3/22
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version：v1.0
 */
public class TokenTypeServiceTest {
    @Mock
    TokenTypeDao dao;
    @InjectMocks
    TokenTypeService tokenTypeService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInsert() {
        tokenTypeService.insert(new TokenType());
    }

    @Test
    public void testDelete() {
        tokenTypeService.delete(Integer.valueOf(0));
    }

    @Test
    public void testUpdate() {
        tokenTypeService.update(new TokenType());
    }

    @Test
    public void testGet() {
        TokenType result = tokenTypeService.get(Integer.valueOf(0));
        Assert.assertEquals(new TokenType(), result);
    }

    @Test
    public void testAll() {
        Object result = tokenTypeService.all();
        Assert.assertEquals(new Object(), result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme