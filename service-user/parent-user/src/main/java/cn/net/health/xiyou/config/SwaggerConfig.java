package cn.net.health.xiyou.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableSwagger2
@Configuration
public class SwaggerConfig {

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //设置文档标题(API名称)
                .title("shiro模块完全交给mongo")
                //文档描述
                .description("mongo")
                //URL
                .termsOfServiceUrl("http://127.0.0.1:8088/")
                //版本号
                .version("1.0.0")
                .build();
    }




    @Bean("系统管理模块")
    public Docket xitongApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("系统管理")
                .select()
                //下面的paths设置该模块拦截的路由
                .paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("cn.net.health.xiyou.controller.shiro"))
                .build()
                .apiInfo(apiInfo())
                ;
    }

    @Bean("全局")
    //当然你要是不想一个一个的设置最简单的是设置一个全局模块，拦截所有的路由
    public Docket quanJuApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("全局")
                //下面的paths设置该模块拦截的路由
                .pathMapping("/")
                .select()
                .build()
                .apiInfo(apiInfo())
                ;
    }


}
