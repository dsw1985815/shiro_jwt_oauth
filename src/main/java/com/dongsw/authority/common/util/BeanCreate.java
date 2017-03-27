package com.dongsw.authority.common.util;

import org.apache.log4j.Logger;
import org.beetl.sql.core.*;
import org.beetl.sql.core.db.DBStyle;
import org.beetl.sql.core.db.MySqlStyle;
import org.beetl.sql.ext.DebugInterceptor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Properties;

/**
 * 生成基本类和sql模板
 * @author Administrator
 *
 */
public class BeanCreate {

	private static Logger logger = Logger.getLogger(BeanCreate.class);

	@Autowired
	Properties resultProperties;

    public void gen() {
		ConnectionSource source = ConnectionSourceHelper.getSimple(
				(String)resultProperties.get("spring.datasource.driver-class-name"),
				(String)resultProperties.get("spring.datasource.url"),
				(String)resultProperties.get("spring.datasource.username"),
				(String)resultProperties.get("spring.datasource.password"));
		
		DBStyle mysql = new MySqlStyle();
		SQLLoader loader = new ClasspathLoader("/sql");
		UnderlinedNameConversion nc = new  UnderlinedNameConversion();
		SQLManager sqlManager = new SQLManager(mysql,loader,source,nc,new Interceptor[]{new DebugInterceptor()});
		
        /**
         * 生成全部 ：sqlManager.genALL("com.dongsw.authority.model", new GenConfig(),new GenFilter());
         **/

		try {
			// 单独生成pojo类
			sqlManager.genPojoCodeToConsole("token_type");
			// 单独生成sql模板
			sqlManager.genSQLTemplateToConsole("token_type");
		} catch (Exception e) {
			logger.error("生成失败",e);
		}

	}

}
