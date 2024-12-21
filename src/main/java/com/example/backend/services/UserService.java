package com.example.backend.services;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.annotation.LogOperationWithId;
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
    public User userInfo(int user_id) {
        try {
            User user = userMapper.findByUserId(user_id);
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
    public User wechatUserInfo(String openid){
        try {
            return userMapper.findByOpenid(openid);
        } catch (Exception e) {
            // 记录异常信息
            e.printStackTrace();
            // 可以选择返回一个特定的错误码或抛出自定义异常
            return null; // 表示查询失败
        }
    }
    public String userInfoAvatar(int user_id) {
        try {
            return userMapper.findAvatarByUserId(user_id);
        } catch (Exception e) {
            // 记录异常信息
            e.printStackTrace();
            // 可以选择返回一个特定的错误码或抛出自定义异常
            return null; // 表示查询失败
        }
    }
    @LogOperationWithId(value="基本信息设置",idParamIndex=3)//user_id是第四个参数
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
    @LogOperationWithId(value="重置密码",idParamIndex=0)
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
    @LogOperationWithId(value="重置邮箱",idParamIndex=0)
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
    @LogOperationWithId(value="请求用户列表",idParamIndex=0)
    public List<User> adminUserInfo(int user_id){
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
    @LogOperationWithId(value="添加用户",idParamIndex=3)
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
    @LogOperationWithId(value="修改用户信息",idParamIndex=3)
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
    @LogOperationWithId(value="删除用户",idParamIndex=0)
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
    @LogOperationWithId(value="微信登录",idParamIndex=0)
    public int loginByWechat(int user_id){
        try{
            User user = userMapper.findByUserId(user_id);
            if(user!=null){
                return 1;
            }
            else{
                return 0;//后续会调用微信用户绑定程序
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }
    @LogOperationWithId(value="微信绑定",idParamIndex=0)
    public int bindByWechat(int user_id,String openid){
        try{
            return userMapper.bindWechatOpenid(user_id,openid);
        }
        catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }
    public User preBindByWechat(String phone) {
        try {
            return userMapper.findByPhone(phone);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}