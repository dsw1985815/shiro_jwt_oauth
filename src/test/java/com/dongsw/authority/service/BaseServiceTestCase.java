/**
 * Author：liaozhanggen
 * Create Date：2017年3月21日
 * Version：v2.0
 */
package com.dongsw.authority.service;

import com.dongsw.authority.BaseTestCase;

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
public class BaseServiceTestCase extends BaseTestCase {
    protected static final String DOMAIN = "service";
    protected static final Class SERVICE_CLASS = BaseServiceTestCase.class;

    protected static <T> T  initParam(String name,Class clazz) throws IOException, ClassNotFoundException {
        return BaseTestCase.initParamByFileName(name,clazz,SERVICE_CLASS);
    }

    protected static <T> T  initParam(Class clazz) throws IOException, ClassNotFoundException {
        return BaseTestCase.initParam(clazz,SERVICE_CLASS);
    }

    protected static boolean prepareParamFile(Class clazz) {
        return BaseTestCase.prepareParamFile(clazz, DOMAIN);
    }


}
