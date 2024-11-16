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
        User user = userMapper.findByEmail(email);
        if (user != null) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if(encoder.matches(password, user.getPassword())){
                return user.getUserId();
            }
        }
        return 0;
    }
    public String role(String email) {
        User user = userMapper.findByEmail(email);
        if (user != null) {
            return user.getRole();
        }
        return null;
    }
    public int register(String email, String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String passwordBCrypt = encoder.encode(password);
        String role="worker";//默认为员工
        return userMapper.insertUser(email, passwordBCrypt,role);
    }
    public int resetPassword(String email, String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String passwordBCrypt = encoder.encode(password);
//        System.out.println(passwordBCrypt);
        return userMapper.updatePassword(email, passwordBCrypt);
    }
}
