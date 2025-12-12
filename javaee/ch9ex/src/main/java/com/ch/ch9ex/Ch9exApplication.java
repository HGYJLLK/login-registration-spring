package com.ch.ch9ex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 应用主启动类
 * 实训9：单元测试练习
 *
 * @SpringBootApplication 是一个组合注解，包含：
 * 1. @SpringBootConfiguration：标记这是一个配置类
 * 2. @EnableAutoConfiguration：启用 Spring Boot 自动配置
 * 3. @ComponentScan：启用组件扫描，自动发现并注册 Bean
 *
 * @version 1.0
 */
@SpringBootApplication
public class Ch9exApplication {

    /**
     * 应用程序入口方法
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 启动 Spring Boot 应用
        SpringApplication.run(Ch9exApplication.class, args);
        System.out.println("========================================");
        System.out.println("实训9：单元测试练习项目启动成功！");
        System.out.println("========================================");
    }
}
