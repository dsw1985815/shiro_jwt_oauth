/**
 * 
 * Author：liaozhanggen
 * Create Date：2017年3月21日
 * Version：v2.0
 */
package com.dongsw.authority.dao;

import com.dongsw.authority.BaseTestCase;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * Comments：
 * Author：dongshuangwei
 * Create Date：2017/3/22
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version：v1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
@Rollback(true)
@FixMethodOrder(MethodSorters.JVM)
@ImportResource(value = "classpath:config/spring-mybatis.xml")
public class BaseDaoTestCase extends BaseTestCase {
    protected static final String DOMAIN = "dao";
    protected static final Class DAO_CLASS = BaseDaoTestCase.class;
    protected static <T> T  initParam(String name,Class clazz) throws IOException, ClassNotFoundException {
        return BaseTestCase.initParamByFileName(name,clazz,DAO_CLASS);
    }

    protected static <T> T  initParam(Class clazz) throws IOException, ClassNotFoundException {
        return BaseTestCase.initParam(clazz,DAO_CLASS);
    }
    protected static <T> T  initParam(Class clazz,Class innerClass) throws IOException, ClassNotFoundException {
        return BaseTestCase.initPageQueryParam(clazz,innerClass,DAO_CLASS);
    }


    protected static boolean prepareParamFile(Class clazz) {
        return BaseTestCase.prepareParamFile(clazz, DOMAIN);
    }
}
