package com.example.backend.services;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.entity.User;
import com.example.backend.mapper.PermissionMapper;
import com.example.backend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PermissionMapper permissionMapper;

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
    //返回登录的用户相关信息
    public User userInfo(String email) {
        try {
            User user = userMapper.findByEmail(email);
            if (user != null) {
                return user;
            }
        } catch (Exception e) {
            // 记录异常信息
            e.printStackTrace();
            // 可以选择返回一个特定的错误码或抛出自定义异常
            return null; // 表示查询失败
        }
        return null; // 用户不存在
    }
    //个人设置信息更改
    public int basicInfoSetting(String username,String phone,String intro,int user_id) {
    try {
        User user = userMapper.findByUserId(user_id);
        if (user != null) {
            return userMapper.updateUserInfo(username, phone, intro, user_id);
        }
        return -1;
    } catch (Exception e) {
        // 记录异常信息
        e.printStackTrace();
        // 可以选择返回一个特定的错误码或抛出自定义异常
        return -1; // 表示更改失败
        }
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
    public int resetPersonalPassword(int user_id,String old_password, String new_password) {
        try {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String newPasswordBCrypt = encoder.encode(new_password);
            User user = userMapper.findByUserId(user_id);
            if(user!=null){
                if (encoder.matches(old_password, user.getPassword())) {
                    return userMapper.updatePersonalPassword(user_id, newPasswordBCrypt);
                }
            }
        } catch (Exception e) {
            // 记录异常信息
            e.printStackTrace();
            // 可以选择返回一个特定的错误码或抛出自定义异常
            return -1; // 表示重置密码失败
        }
        return 0; // 输入原来密码错误
    }
    public int resetPersonalEmail(int user_id,String oldEmail, String newEmail) {
        try {
            if (userMapper.findByUserId(user_id) == null) {
                return 0;
            }
            return userMapper.updatePersonalEmail(user_id,oldEmail, newEmail);
        } catch (Exception e) {
            // 记录异常信息
            e.printStackTrace();
            // 可以选择返回一个特定的错误码或抛出自定义异常
            return -1; // 表示重置密码失败
        }
    }
    public List<User> adminUserInfo(int user_id){
//        System.out.println("=================[userService/adminUserInfo]=====================");
        try {
            User user = userMapper.findByUserId(user_id);
            String role = user.getRole();
            if (role.equals("admin")) {
                return userMapper.findAllUser();
            }
            if (role.equals("manager")) {
                return userMapper.findDepartmentUser(user.getDepartment());
            }
            return null;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public int adminUserAdd(String username,String department,String role,int adminId){
        try {
            User user = userMapper.findByUserId(adminId);
            String adminRole = user.getRole();
            String password = "123456";
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String passwordBCrypt = encoder.encode(password);
            if (adminRole.equals("admin")){
                return userMapper.adminInsertUser(username,passwordBCrypt,department,role);
            }
            return -1;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }
    public int adminUserUpdate(String username,String department,String role,int adminId,int user_id){
        try {
            User user = userMapper.findByUserId(adminId);
            String adminRole = user.getRole();
            if (adminRole.equals("admin")){
                return userMapper.adminUpdateUserInfo(username,department,role,user_id);
            }
            return -1;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }
    public int adminUserDelete(int adminId,int user_id){
        try {
            User user = userMapper.findByUserId(adminId);
            String adminRole = user.getRole();
            if (adminRole.equals("admin")){
                return userMapper.adminDeleteUser(user_id);
            }
            return -1;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }
}