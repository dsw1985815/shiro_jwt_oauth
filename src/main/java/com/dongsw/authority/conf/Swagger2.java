package com.dongsw.authority.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2 {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.dongsw.authority"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 有空可以考虑根据version列出接口版本差异,参考flyway的方式 -by 戚羿辰
     * @return
     */
    private ApiInfo apiInfo() {
    	
        return  new ApiInfoBuilder()
        .title("趋恒服务端 消息队列 APIs")
        .description("API文档")
        .contact("summer_last")
        .version("1.0")
        .build();
    }

}
