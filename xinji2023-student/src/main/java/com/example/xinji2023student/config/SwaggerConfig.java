//package com.example.xinji2023student.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.oas.annotations.EnableOpenApi;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//
//@Configuration
//@EnableOpenApi // 启用 OpenAPI 规范（Swagger3）
//public class SwaggerConfig {
//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.OAS_30)  // 使用 OpenAPI 3.0
//                .apiInfo(apiInfo())
//                .select()
//                // 扫描控制器包路径（确保与项目实际包一致）
//                .apis(RequestHandlerSelectors.basePackage("com.example.xinji2023student.controller"))
//                .paths(PathSelectors.any())
//                .build();
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("学生管理系统 API")
//                .description("包含学生、课程、部门等模块接口文档")
//                .version("1.0")
//                .build();
//    }
//}