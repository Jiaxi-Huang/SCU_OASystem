package com.example.backend.controllers;

import com.example.backend.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class Auth {

    private static final Logger logger = LoggerFactory.getLogger(Auth.class);

    @Autowired
    private UserService userService;

    @PostMapping("/user/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        try {
            String email = request.getEmail();
            String password = request.getPassword();

            boolean isAuthenticated = userService.login(email, password);
            if (isAuthenticated) {
                return ResponseEntity.status(200).body("000");
            } else {
                return ResponseEntity.status(401).body("无效的用户名或密码");
            }
        } catch (Exception e) {
            logger.error("处理登录请求时发生错误: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器内部错误");
        }
    }

    // 定义请求体的数据模型
    public static class LoginRequest {
        private String email;
        private String password;

        // Getters and Setters
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
