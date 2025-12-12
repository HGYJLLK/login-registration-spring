package com.example.login.controller;

import com.example.login.model.User;
import com.example.login.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 获取所有用户
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    // 更新用户信息
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody Map<String, String> updates) {
        return userRepository.findById(id)
                .map(user -> {
                    if (updates.containsKey("name")) {
                        user.setName(updates.get("name"));
                    }
                    if (updates.containsKey("username")) {
                        user.setUsername(updates.get("username"));
                    }
                    if (updates.containsKey("password") && !updates.get("password").isEmpty()) {
                        user.setPassword(updates.get("password"));  // 明文存储（仅用于作业）
                    }
                    if (updates.containsKey("gender")) {
                        user.setGender(updates.get("gender"));
                    }
                    if (updates.containsKey("phonenumber")) {
                        user.setPhonenumber(updates.get("phonenumber"));
                    }
                    if (updates.containsKey("avatarUrl")) {
                        user.setAvatarUrl(updates.get("avatarUrl"));
                    }
                    if (updates.containsKey("email")) {
                        user.setEmail(updates.get("email"));
                    }
                    userRepository.save(user);
                    return ResponseEntity.ok(Map.of(
                            "message", "更新成功",
                            "success", true
                    ));
                })
                .orElse(ResponseEntity.badRequest().body(Map.of(
                        "message", "用户不存在",
                        "success", false
                )));
    }

    // 删除用户
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.ok(Map.of(
                    "message", "删除成功",
                    "success", true
            ));
        } else {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "用户不存在",
                    "success", false
            ));
        }
    }
}
