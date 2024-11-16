package com.example.backend.services;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.entity.User;
import com.example.backend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    @Autowired
    private UserMapper userMapper;

    public int login(String email, String password) {
        try {
            User user = userMapper.findByEmail(email);
            if (user != null) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                if (encoder.matches(password, user.getPassword())) {
                    return user.getUserId();
                }
            }
        } catch (Exception e) {
            // 记录异常信息
            e.printStackTrace();
            // 可以选择返回一个特定的错误码或抛出自定义异常
            return -1; // 表示登录失败
        }
        return 0; // 用户名或密码错误
    }

    public String role(String email) {
        try {
            User user = userMapper.findByEmail(email);
            if (user != null) {
                return user.getRole();
            }
        } catch (Exception e) {
            // 记录异常信息
            e.printStackTrace();
            // 可以选择返回一个特定的错误码或抛出自定义异常
            return null; // 表示查询失败
        }
        return null; // 用户不存在
    }

    public int register(String email, String password) {
        try {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String passwordBCrypt = encoder.encode(password);
            String role = "worker"; // 默认为员工
            return userMapper.insertUser(email, passwordBCrypt, role);
        } catch (Exception e) {
            // 记录异常信息
            e.printStackTrace();
            // 可以选择返回一个特定的错误码或抛出自定义异常
            return -1; // 表示注册失败
        }
    }

    public int resetPassword(String email, String password) {
        try {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String passwordBCrypt = encoder.encode(password);
            return userMapper.updatePassword(email, passwordBCrypt);
        } catch (Exception e) {
            // 记录异常信息
            e.printStackTrace();
            // 可以选择返回一个特定的错误码或抛出自定义异常
            return -1; // 表示重置密码失败
        }
    }
}