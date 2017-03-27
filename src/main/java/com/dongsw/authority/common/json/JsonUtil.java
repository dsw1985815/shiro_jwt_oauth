package com.dongsw.authority.common.json;

import com.dongsw.authority.common.def.ResultCode;
import com.dongsw.authority.common.util.StringUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * json处理工具类
 *
 * @author 顾文伟
 */
public class JsonUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    private JsonUtil() {

    }

    /**
     * Object -> json
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            logger.error("Object转化成Json失败", e);
        }
        return null;
    }

    /**
     * json -> Object
     *
     * @param <T>
     * @param content
     * @param valueType
     * @return
     */
    public static <T> T toObject(String content, Class<T> valueType) {
        if (StringUtils.isEmpty(content)) {
            return null;
        }
        try {
            return mapper.readValue(content, valueType);
        } catch (Exception e) {
            logger.error("Json转化成Object失败", e);
        }
        return null;
    }

    /**
     * 将json字符转为自定义的对象
     *
     * @author liaozhanggen 2016年12月26日
     */
    public static <T> T jsonToInstance(String message, Class<T> valueType) {
        Gson gson = new Gson();
        return  gson.fromJson(message, valueType);
    }

    /**
     * 将 JsonResult 反回的json形式，转成List<T>需要的实例集合，工具
     * JSON
     * {
     * data{
     * pageNum : 1,
     * pageSize : 2,
     * ...
     * list:[{
     * id : 1,
     * name: "liaozhanggen",
     * sex: 1
     * ...
     * },{
     * ...
     * ...
     * }]
     * }
     * }
     * <p>
     * ==>转为如下结果
     * <p>
     * [{
     * id: 1,
     * name: "liaozhnaggen"
     * },{
     * id: 1,
     * name: "zhangsan"
     * }]
     *
     * @author liaozhanggen 2016年12月24日
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> List<T> jsonToArray(String result, Class<T> valueType) {
        List<T> arr = new ArrayList<>();
        Gson gson = new Gson();
        Map<String, Map> param = gson.fromJson(result, Map.class);
        Map dataStr = param.get("data");
        List<Map> listMapper = (List) dataStr.get("list");
        for (Map object : listMapper) {
            String tt = gson.toJson(object);
            arr.add(gson.fromJson(tt, valueType));
        }
        return arr;
    }

    /**
     * json -> List
     *
     * @param <T>
     * @param content
     * @param valueType
     * @return
     */
    public static <T> List<T> toList(String content, Class<T> valueType) {
        JsonNode node = null;
        try {
            node = mapper.readTree(content);
        } catch (Exception e) {
            logger.error("Json转化成List失败", e);
        }
        if (node == null) {
            return new ArrayList<>(0);
        }
        Iterator<JsonNode> iterator = node.elements();
        List<T> list = new ArrayList<>();
        while (iterator.hasNext()) {
            String json = iterator.next().toString();
            T e = toObject(json, valueType);
            list.add(e);
        }
        return list;
    }

    /**
     * success json
     *
     * @return - {success:true}
     */
    public static String successJson() {
        return toJson(new JsonResult());
    }

    /**
     * result json
     *
     * @param success - 操作成功与否
     * @param message - 结果信息
     * @return - {success:true,message:'test'}
     */
    public static String resultJson(boolean success, String message) {
        return toJson(new JsonResult(message, success ? ResultCode.SUCCESS : ResultCode.FAILED));
    }

    /**
     * result json
     *
     * @param code    - 状态码
     * @param message - 结果信息
     * @param data    - 结果数据
     * @return
     */
    public static String resultJson(int code, String message, Object data) {
        return toJson(new JsonResult(data, message, code));
    }


    /**
     * 复杂类型：Json转化为Map<br>
     *
     * @param jsonString
     * @return
     */
    public static Map<String, String> jsonToJavaMap(String jsonString) throws IOException {
        return mapper.readValue(jsonString, Map.class);
    }
}