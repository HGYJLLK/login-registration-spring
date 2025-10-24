package com.example.login.service;

import com.example.login.model.User;
import com.example.login.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean register(String username, String password, String name, String gender, String phonenumber) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(username)) {
            return false;
        }

        // 创建新用户 - 密码明文存储（仅用于作业）
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);  // 明文存储
        user.setName(name);
        user.setGender(gender);
        user.setPhonenumber(phonenumber);

        userRepository.save(user);
        return true;
    }

    public boolean authenticate(String username, String password) {
        return userRepository.findByUsername(username)
                .map(user -> password.equals(user.getPassword()))  // 明文比较
                .orElse(false);
    }
}