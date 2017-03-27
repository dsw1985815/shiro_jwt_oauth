package com.dongsw.authority.service;

import com.dongsw.authority.common.exception.IllegalDataException;
import com.dongsw.authority.conf.Audience;
import com.dongsw.authority.dao.ResourceDao;
import com.dongsw.authority.model.Resource;
import com.dongsw.authority.model.TokenType;
import org.beetl.sql.core.engine.PageQuery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

/**
 * Comments：
 * Author：dongshuangwei
 * Create Date：2017/3/22
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version：v1.0
 */
public class ResourceServiceTest {
    @Mock
    ResourceDao dao;
    @Mock
    Audience audience;
    @InjectMocks
    ResourceService resourceService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInsert() {
        resourceService.insert(new Resource());
    }

    @Test
    public void testDelete() {
        resourceService.delete(Integer.valueOf(0));
    }

    @Test
    public void testUpdate() {
        resourceService.update(new Resource());
    }

    @Test
    public void testGet() {
        Resource result = resourceService.get(Integer.valueOf(0));
        Assert.assertEquals(new Resource(), result);
    }

    @Test
    public void testQuery() {
        PageQuery<Resource> result = resourceService.query(new Resource(), Integer.valueOf(0), Integer.valueOf(0));
        Assert.assertEquals(new PageQuery<Resource>(0L, new Object(), "userDefinedOrderBy", 0L), result);
    }

    @Test
    public void testFindResourcePermission() {
        Map<String, String> result = resourceService.findResourcePermission();
        Assert.assertEquals(new HashMap<String, String>() {
            {
                put("String", "String");
            }
        }, result);
    }

    @Test
    public void testGetResourceDetail() {
        Resource result = null;
        try {
            result = resourceService.getResourceDetail(new Resource());
        } catch (IllegalDataException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(new Resource(), result);
    }

    @Test
    public void testGetTokenType() {
        TokenType result = null;
        try {
            result = resourceService.getTokenType("uri");
        } catch (IllegalDataException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(new TokenType(), result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme