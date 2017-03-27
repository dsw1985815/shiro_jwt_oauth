package com.dongsw.authority;

import com.dongsw.authority.common.exception.IllegalDataException;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Comments：参数文件生成器
 * Author：dongshuangwei
 * Create Date：2017/3/23
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version：v1.0
 */
class ParamFileGenerator {


    private static Logger logger = Logger.getLogger(ParamFileGenerator.class);
    private static JsonFactory jsonFactory;

    private static final String DEFAULT_PAGE_QUERY_CLASS = "org.beetl.sql.core.engine.PageQuery";
    private static final String DEFAULT_PACKAGE_PREFIX = "com.dongsw.authority";
    private static final String DEFAULT_PARAM_PACKAGE = "model";


    static {
        ObjectMapper objectMapper = new ObjectMapper();
        jsonFactory = objectMapper.getJsonFactory();
        jsonFactory.setCodec(objectMapper);
    }

    private ParamFileGenerator() {
    }

    /**
     * 生成json参数文件
     *
     * @param c          需要被生成属性的类对象
     * @param backupFile 是否需要创建新文件并备份原有文件
     * @param seed       是否需要参数值同一源头
     * @throws Exception
     */
    private static void genNormalJsonFile(Class c, boolean backupFile, boolean seed, String packagePrefix, String paramPackage, String jsonFilePath) throws Exception {
        Field[] fields = c.getDeclaredFields();
        for (Field f : fields) {
            String type = f.getGenericType().toString(); // 获取属性的类型
            if (type.contains("class " + packagePrefix + "." + paramPackage)) {
                Object o = f.getType().newInstance();
                genField(o, seed);
                genJsonFile(o, packagePrefix + "." + jsonFilePath, backupFile);
            }
        }
        logger.info("参数文件配置完成");
    }

    /**
     * 生成当前类的全部测试json文件
     *
     * @param c
     * @param jsonFilePath
     * @throws Exception
     */
    public static void genJsonFile(Class c, String jsonFilePath) throws Exception {
        genNormalJsonFile(c, jsonFilePath);
        genPageQueryFile(c, jsonFilePath);
    }

    /**
     * 生成当前类的
     *
     * @param c
     * @param jsonFilePath
     * @throws Exception
     */
    public static void genPageQueryFile(Class c, String jsonFilePath) throws Exception {
        genPageQueryFile(c, false, false, DEFAULT_PAGE_QUERY_CLASS, DEFAULT_PACKAGE_PREFIX, jsonFilePath);
    }

    public static void genNormalJsonFile(Class c, String jsonFilePath) throws Exception {
        genNormalJsonFile(c, false, false, DEFAULT_PACKAGE_PREFIX, DEFAULT_PARAM_PACKAGE, jsonFilePath);
    }

    /**
     * 生成分页查询属性json参数文件
     *
     * @param c          需要被生成参数的对象
     * @param backupFile 是否需要创建新文件并备份原有文件
     * @param seed       是否需要参数值同一源头
     * @throws Exception
     */
    private static void genPageQueryFile(Class c, boolean backupFile, boolean seed, String queryPackage, String packagePrefix, String jsonFilePath) throws Exception {
        Field[] fields = c.getDeclaredFields();
        for (Field f : fields) {
            String type = f.getGenericType().toString(); // 获取属性的类型
            if (type.contains(queryPackage)) {
                Object o = f.getType().newInstance();
                String genType = type.substring(type.indexOf('<') + 1, type.indexOf('>'));
                genPageQueryField(o, genType, seed);
                genPageQueryJsonFile(o, genType, backupFile, packagePrefix, jsonFilePath);
            }
        }
        logger.info("分页参数文件配置完成");
    }

    private static void genField(Object model, boolean seed) throws Exception {
        Field[] fields = model.getClass().getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
        Random r = new Random();
        try {
            for (Field field : fields) { // 遍历所有属性
                initField(field, model, seed, r);
            }
        } catch (Exception e) {
            logger.error("生成随机属性异常", e);
            throw new IllegalDataException(e);
        }
    }

    private static void initField(Field field, Object model, boolean seed, Random r) throws Exception {
        String name = field.getName(); // 获取属性的名字
        name = name.substring(0, 1).toUpperCase() + name.substring(1); // 将属性的首字符大写，方便构造get，set方法
        String method = "set" + name;
        Method[] methods = model.getClass().getDeclaredMethods();
        if (!methodExist(methods, method)) {
            return;
        }
        String type = field.getGenericType().toString(); // 获取属性的类型
        if ("class java.lang.String".equals(type)) { // 如果type是类类型，则前面包含"class "，后面跟类名
            Method m = model.getClass().getMethod("get" + name);
            String value = (String) m.invoke(model); // 调用getter方法获取属性值
            if (value == null) {
                m = model.getClass().getMethod("set" + name, String.class);
                m.invoke(model, RandomStringUtil.getRandomCode(r.nextInt(20), 6, seed));
            }
        }
        if ("class java.lang.Integer".equals(type)) {
            Method m = model.getClass().getMethod("get" + name);
            Integer value = (Integer) m.invoke(model);
            if (value == null) {
                m = model.getClass().getMethod("set" + name, Integer.class);
                m.invoke(model, r.nextInt(999999999));
            }
        }

        if ("class java.lang.Long".equals(type)) {
            Method m = model.getClass().getMethod("get" + name);
            Long value = (Long) m.invoke(model);
            if (value == null) {
                m = model.getClass().getMethod("set" + name, Long.class);
                m.invoke(model, r.nextLong());
            }
        }

        if ("class java.lang.Boolean".equals(type)) {
            Method m = model.getClass().getMethod("get" + name);
            Boolean value = (Boolean) m.invoke(model);
            if (value == null) {
                m = model.getClass().getMethod("set" + name, Boolean.class);
                m.invoke(model, r.nextInt(1) / 2);
            }
        }
        if ("class java.util.Date".equals(type)) {
            Method m = model.getClass().getMethod("get" + name);
            Date value = (Date) m.invoke(model);
            if (value == null) {
                m = model.getClass().getMethod("set" + name, Date.class);
                m.invoke(model, new Date());
            }
        }
        if (type.contains("class " + DEFAULT_PACKAGE_PREFIX + "." + DEFAULT_PARAM_PACKAGE)) {
            Object o = field.getType().newInstance();
            genField(o, seed);
            Method m = model.getClass().getMethod("get" + name);
            Object value = m.invoke(model);
            if (value == null) {
                m = model.getClass().getMethod("set" + name, field.getType());
                m.invoke(model, o);
            }
        }
    }

    private static boolean methodExist(Method[] methods, String method) {
        for (Method m : methods) {
            if (m.getName().equals(method)) {
                return true;
            }
        }
        return false;
    }

    private static void genPageQueryJsonFile(Object o, String type, boolean backupFile, String packagePrefix, String jsonFilePath) throws Exception {
        String basedir = System.getProperty("user.dir") + "/src/test/resources/";
        String dir = (packagePrefix + "." + jsonFilePath).replace('.', '/');
        String filename = "PageQuery" + type.substring(type.lastIndexOf('.') + 1, type.length()) + ".json";
        File jsonfile = createOrbackupFile(basedir, dir, filename, backupFile);
        writeJson(jsonfile, o);
    }

    private static void writeJson(File jsonfile, Object o) throws IOException {
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(jsonfile, JsonEncoding.UTF8);
        jsonGenerator.useDefaultPrettyPrinter();
        jsonGenerator.writeObject(o);
        jsonGenerator.flush();
        jsonGenerator.close();
    }

    private static void genPageQueryField(Object model, String genType, boolean seed) throws Exception {
        Object o1 = Class.forName(genType).newInstance();
        genField(o1, seed);
        Method m = model.getClass().getMethod("setParas", Object.class);
        m.invoke(model, o1);
    }

    private static void genJsonFile(Object o, String packagePath, boolean backupFile) throws Exception {

        String basedir = System.getProperty("user.dir") + "/src/test/resources/";
        String dir = packagePath.replace('.', '/');
        String filename = o.getClass().getSimpleName() + ".json";
        File jsonfile = createOrbackupFile(basedir, dir, filename, backupFile);
        writeJson(jsonfile, o);
    }


    /**
     * 用于文件备份的类
     * <p>
     * 适用于各种类型文件备份，在原文件的路径下，创建备份文件，命名为 原文件名.bak
     * <p>
     * 实现文件复制的函数
     * <p>
     * 采用二进制流的形式来实现文件的读写
     */
    private static void fileCopy(File srcFile, File destFile) {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        InputStream src = null;
        OutputStream dest = null;
        try {
            fileInputStream = new FileInputStream(srcFile);
            fileOutputStream = new FileOutputStream(destFile);
            src = new BufferedInputStream(fileInputStream);
            dest = new BufferedOutputStream(fileOutputStream);

            byte[] trans = new byte[1024];

            int count;

            while ((count = src.read(trans)) != -1) {
                dest.write(trans, 0, count);
            }

            dest.flush();

        } catch (IOException e) {
            logger.error("生成参数文件异常", e);
        } finally {
            try {
                fileInputStream.close();
                fileOutputStream.close();
                src.close();
                dest.close();
            } catch (IOException e) {
                logger.error("关闭流出现异常，不做操作", e);
            }
        }

    }

    /**
     * 备份文件，在原文件目录下创建备份文件，命名为 原文件名.bak
     *
     * @return true 成功，false 失败
     */
    private static File createOrbackupFile(String basedir, String dir, String filename, boolean backupFile) throws Exception {
        File folder = new File(basedir + dir);
        // 创建文件夹
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File jsonfile = new File(basedir + dir, filename);

        if (!jsonfile.exists()) {
            if (jsonfile.createNewFile()) {
                return jsonfile;
            } else {
                throw new IllegalDataException("创建文件失败");
            }
        } else {
            if (backupFile) {
                //创建备份文件
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                File backUpFile = new File(jsonfile.getAbsolutePath() + "." + simpleDateFormat.format(new Date()) + ".json");

                if (backUpFile.createNewFile()) {
                    //创建备份文件成功，进行文件复制
                    fileCopy(jsonfile, backUpFile);
                }
            }
            return jsonfile;
        }
    }

}
