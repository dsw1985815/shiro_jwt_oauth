package com.dongsw.authority.service;

import com.dongsw.authority.dao.ResourceFiltersDao;
import com.dongsw.authority.model.ResourceFilters;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Comments：
 * Author：dongshuangwei
 * Create Date：2017/3/22
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version：v1.0
 */
public class ResourceFiltersServiceTest {
    @Mock
    ResourceFiltersDao dao;
    @Mock
    RedisTemplate<Serializable, Object> template;
    @InjectMocks
    ResourceFiltersService resourceFiltersService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInsert()  {
        resourceFiltersService.insert(new ResourceFilters());
    }

    @Test
    public void testDelete()  {
        resourceFiltersService.delete(Integer.valueOf(0));
    }

    @Test
    public void testUpdate()  {
        resourceFiltersService.update(new ResourceFilters());
    }

    @Test
    public void testGet()  {
        ResourceFilters result = resourceFiltersService.get(Integer.valueOf(0));
        Assert.assertEquals(new ResourceFilters(), result);
    }

    @Test
    public void testAll()  {
        List<ResourceFilters> result = resourceFiltersService.all();
        Assert.assertEquals(Arrays.<ResourceFilters>asList(new ResourceFilters()), result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme