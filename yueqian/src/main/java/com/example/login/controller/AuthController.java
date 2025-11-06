package com.example.login.controller;

import com.example.login.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> requestBody) {
        try {
            String username = requestBody.get("username");
            String password = requestBody.get("password");
            String name = requestBody.get("name");
            String gender = requestBody.get("gender");
            String phonenumber = requestBody.get("phonenumber");

            if (username == null || password == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "message", "用户名和密码不能为空",
                        "success", false
                ));
            }

            boolean success = authService.register(
                    username,
                    password,
                    name != null ? name : username,
                    gender,
                    phonenumber
            );

            if (success) {
                return ResponseEntity.ok(Map.of(
                        "message", "注册成功",
                        "success", true
                ));
            } else {
                return ResponseEntity.badRequest().body(Map.of(
                        "message", "用户名已存在",
                        "success", false
                ));
            }
        } catch (Exception e) {
            // 打印详细错误信息到日志
            e.printStackTrace();
            // 向客户端返回友好的错误信息
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", "注册失败：" + e.getMessage(),
                            "success", false
                    ));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> requestBody) {
        String username = requestBody.get("username");
        String password = requestBody.get("password");

        if (username == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "用户名和密码不能为空",
                    "success", false
            ));
        }

        boolean success = authService.authenticate(username, password);

        if (success) {
            return ResponseEntity.ok(Map.of(
                    "message", "登录成功",
                    "success", true
            ));
        } else {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "用户名或密码错误",
                    "success", false
            ));
        }
    }
}