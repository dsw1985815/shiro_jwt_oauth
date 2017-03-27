package com.dongsw.authority;

import com.dongsw.authority.dao.AuthorityDaoTest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

/**
 * Comments：
 * Author：dongshuangwei
 * Create Date：2017/3/22
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version：v1.0
 */
public class BaseTestCase {
    private static Logger logger = Logger.getLogger(AuthorityDaoTest.class);
    private static ObjectMapper mapper ;
    static {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private static InputStream getClassPathResource(String path, Class c) throws IOException {
        return new ClassPathResource(path, c).getInputStream();
    }

    /**
     * 通过文件名转化对象
     * @param name 文件名
     * @param clazz 目标类 即被反射并初始化属性的类
     * @param classPath  文件类路径
     * @param <T>
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    protected static <T> T  initParamByFileName(String name,Class clazz,Class classPath) throws IOException, ClassNotFoundException {
        return (T)mapper.readValue(getClassPathResource(name,classPath), clazz);
    }

    /**
     * 默认以类名作为文件名转化对象
     * @param clazz 目标类 即被反射并初始化属性的类
     * @param classPath 文件类路径
     * @param <T>
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    protected static <T> T  initParam(Class clazz,Class classPath) throws IOException, ClassNotFoundException {
        return (T)mapper.readValue(getClassPathResource(clazz.getSimpleName()+".json",classPath), clazz);
    }

    /**
     * 转化带有泛型实体参数的分页查询对象
     * @param clazz 目标类 即被反射并初始化属性的类
     * @param classPath 文件类路径
     * @param dataClass 分页查询类数据实体类
     * @param <T>
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    protected static <T> T  initPageQueryParam(Class clazz,Class dataClass,Class classPath) throws IOException, ClassNotFoundException {
        return (T)mapper.readValue(getClassPathResource(clazz.getSimpleName()+dataClass.getSimpleName()+".json",classPath), clazz);
    }

    /**
     * 准备参数文件
     * @param clazz 标类 即被反射并初始化属性的类
     * @param domain 包范围
     * @return
     */
    protected static boolean prepareParamFile(Class clazz,String domain){
        try {
            ParamFileGenerator.genJsonFile(clazz, domain);
            return true;
        } catch (Exception e1) {
            logger.error("参数文件生成失败，请检查代码", e1);
            return false;
        }
    }
}
