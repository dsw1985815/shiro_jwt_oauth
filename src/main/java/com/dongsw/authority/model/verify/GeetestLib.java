package com.dongsw.authority.model.verify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GeetestLib {


    private static final Logger logger = LoggerFactory.getLogger(GeetestLib.class);

    private static final String APIURL = "http://api.geetest.com"; //极验验证API URL

    private static final String REGISTERURL = "/register.php"; //register url

    /**
     * 极验验证二次验证表单数据 chllenge
     */
    public static final String FN_GEETEST_CHALLENGE = "geetest_challenge";

    /**
     * 极验验证二次验证表单数据 validate
     */
    public static final String FN_GEETEST_VALIDATE = "geetest_validate";

    /**
     * 极验验证二次验证表单数据 seccode
     */
    public static final String FN_GEETEST_SECCODE = "geetest_seccode";

    /**
     * 公钥
     */
    private String captchaId = "";

    /**
     * 私钥
     */

    private String privateKey = "";

    private String userId = "";

    private String responseStr = "";


    /**
     * 调试开关，是否输出调试日志
     */
    private boolean debugCode = true;

    /**
     * 带参数构造函数
     *
     * @param captchaId
     * @param privateKey
     */
    public GeetestLib(String captchaId, String privateKey) {
        this.captchaId = captchaId;
        this.privateKey = privateKey;
    }

    /**
     * 获取本次验证初始化返回字符串
     *
     * @return 初始化结果
     */
    public String getResponseStr() {
        return responseStr;
    }

    /**
     * 预处理失败后的返回格式串
     *
     * @return
     */
    private String getFailPreProcessRes() {

        Long rnd1 = Math.round(Math.random() * 100);
        Long rnd2 = Math.round(Math.random() * 100);
        String md5Str1 = md5Encode(rnd1 + "");
        String md5Str2 = md5Encode(rnd2 + "");
        String challenge = md5Str1 + md5Str2.substring(0, 2);

        return String.format(
                "{\"success\":%s,\"gt\":\"%s\",\"challenge\":\"%s\"}", 0,
                this.captchaId, challenge);
    }

    /**
     * 预处理成功后的标准串
     */
    private String getSuccessPreProcessRes(String challenge) {

        gtlog("challenge:" + challenge);
        return String.format(
                "{\"success\":%s,\"gt\":\"%s\",\"challenge\":\"%s\"}", 1,
                this.captchaId, challenge);
    }

    /**
     * 输出debug信息，需要开启debugCode
     *
     * @param message
     */
    public void gtlog(String message) {
        if (debugCode) {
            logger.info("gtlog: " + message);
        }
    }


    /**
     * 验证初始化预处理
     *
     * @return 1表示初始化成功，0表示初始化失败
     */
    public int preProcess() {

        if (registerChallenge() != 1) {

            this.responseStr = this.getFailPreProcessRes();
            return 0;
        }

        return 1;

    }

    /**
     * 验证初始化预处理
     *
     * @param userid
     * @return 1表示初始化成功，0表示初始化失败
     */
    public int preProcess(String userid) {

        this.userId = userid;
        return this.preProcess();
    }


    /**
     * 用captchaID进行注册，更新challenge
     *
     * @return 1表示注册成功，0表示注册失败
     */
    private int registerChallenge() {
        try {
            String getUrl = APIURL + REGISTERURL + "?gt=" + this.captchaId;
            if (this.userId != "") {
                getUrl = getUrl + "&user_id=" + this.userId;
                this.userId = "";
            }
            gtlog("GET_URL:" + getUrl);
            String resultStr = readContentFromGet(getUrl);
            gtlog("register_result:" + resultStr);
            if (32 == resultStr.length()) {

                this.responseStr = this.getSuccessPreProcessRes(this.md5Encode(resultStr + this.privateKey));

                return 1;
            } else {
                gtlog("gtServer register challenge failed");
                return 0;
            }
        } catch (IOException e) {
            logger.error("gtlog: exception:register api",e);
        }
        return 0;
    }

    /**
     * 发送请求，获取服务器返回结果
     *
     * @param getURL
     * @return 服务器返回结果
     * @throws IOException
     */
    private String readContentFromGet(String getURL) throws IOException {

        URL getUrl = new URL(getURL);
        HttpURLConnection connection = (HttpURLConnection) getUrl
                .openConnection();

        connection.setConnectTimeout(2000);// 设置连接主机超时（单位：毫秒）
        connection.setReadTimeout(2000);// 设置从主机读取数据超时（单位：毫秒）

        // 建立与服务器的连接，并未发送数据
        connection.connect();

        // 发送数据到服务器并使用Reader读取返回的数据
        StringBuilder sBuffer = new StringBuilder();

        InputStream inStream ;
        byte[] buf = new byte[1024];
        inStream = connection.getInputStream();
        for (int n; (n = inStream.read(buf)) != -1; ) {
            sBuffer.append(new String(buf, 0, n, "UTF-8"));
        }
        inStream.close();
        connection.disconnect();// 断开连接

        return sBuffer.toString();
    }

    /**
     * md5 加密
     *
     * @param plainText
     * @return
     * @time 2014年7月10日 下午3:30:01
     */
    private String md5Encode(String plainText) {
        String reMd5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte[] b = md.digest();
            int i;
            StringBuilder buf = new StringBuilder("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            reMd5 = buf.toString();

        } catch (NoSuchAlgorithmException e) {
            logger.error("md5转化异常",e);
        }
        return reMd5;
    }

}
