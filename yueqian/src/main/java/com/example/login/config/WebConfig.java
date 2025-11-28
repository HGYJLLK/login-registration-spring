package com.example.login.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置music-server静态资源访问
        // 将 /music-server/** 的请求映射到项目根目录下的 music-server 文件夹
        registry.addResourceHandler("/music-server/**")
                .addResourceLocations("file:music-server/")
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));
    }
}
