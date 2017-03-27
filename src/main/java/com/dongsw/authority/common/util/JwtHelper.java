/**
 * Comments：
 * Author：dongshuangwei
 * Create Date：2017/2/16
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version：v1.0
 */
package com.dongsw.authority.common.util;

import com.dongsw.authority.model.LoginPara;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.log4j.Logger;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

public class JwtHelper {

    private static Logger logger = Logger.getLogger(JwtHelper.class);

    private JwtHelper() {
    }


    public static Claims parseJWT(String jsonWebToken, String base64Security) {
        try {
            return Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(base64Security))
                    .parseClaimsJws(jsonWebToken).getBody();
        } catch (Exception ex) {
            logger.info("解析token失败", ex);
            return null;
        }
    }

    public static String createJWT(String cilentId,long ttlmillis, Integer roleId, String userCode, String base64Security,Claims claims
    ) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(base64Security);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                .claim("name", claims.get("name"))
                .claim("userid", claims.get("userid"))
                .claim("userCode", userCode)
                .claim("password", claims.get("password"))
                .claim("checkTimes", claims.get("checkTimes"))
                .claim("resourcePath", claims.get("resourcePath"))
                .claim("required", claims.get("required"))
                .claim("userPlat", claims.get("userPlat"))
                .claim("loginPlat", claims.get("loginPlat"))
                .claim("roleId", roleId)
                .signWith(signatureAlgorithm, signingKey)
                .setId(cilentId);
        //添加Token过期时间
        if (ttlmillis >= 0) {
            long expMillis = nowMillis + ttlmillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp).setNotBefore(now);
        }
        //生成JWT
        return builder.compact();
    }

    public static String createJWT(LoginPara loginPara) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        //生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(loginPara.getBase64Security());
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                .claim("name", loginPara.getUserName())
                .claim("userid", loginPara.getUserId())
                .claim("userCode", loginPara.getUserCode())
                .claim("password", loginPara.getPassword())
                .claim("checkTimes", loginPara.getCheckTimes())
                .claim("resourcePath", loginPara.getRedirectUri())
                .claim("required", loginPara.getRequired())
                .claim("userPlat", loginPara.getUserPlat())
                .claim("loginPlat", loginPara.getLoginPlat())
                .claim("platformId", loginPara.getPlatformId())
                .claim("roleId", loginPara.getRoleId())
                .claim("needVerify", loginPara.getNeedVerify())
                .signWith(signatureAlgorithm, signingKey)
                .setId(loginPara.getClientId());
        //添加Token过期时间
        if (loginPara.getTtlmillis() >= 0) {
            long expMillis = nowMillis + loginPara.getTtlmillis();
            Date exp = new Date(expMillis);
            builder.setExpiration(exp).setNotBefore(now);
        }
        //生成JWT
        return builder.compact();
    }
}