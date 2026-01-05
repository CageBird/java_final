package com.example.xinji2023student.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    // 方案1：升级为 CorsFilter（更通用，解决预检请求问题）
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 1. 允许前端实际地址（添加5173，保留8081兼容）
        config.addAllowedOrigin("http://localhost:8081");
        config.addAllowedOrigin("http://localhost:5173");
        // 2. 允许携带Cookie（前后端分离必备）
        config.setAllowCredentials(true);
        // 3. 允许所有请求方法（GET/POST/PUT/DELETE/OPTIONS）
        config.addAllowedMethod("*");
        // 4. 允许所有请求头（解决自定义头被拦截）
        config.addAllowedHeader("*");
        // 5. 预检请求缓存时间（避免频繁OPTIONS请求）
        config.setMaxAge(3600L);

        // 应用到所有接口
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    // 注释原有的 WebMvcConfigurer 方式（避免冲突）
    /*
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:8081")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(3600);
            }
        };
    }
    */
}