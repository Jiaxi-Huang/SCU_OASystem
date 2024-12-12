package com.example.backend.services;

import com.example.backend.mapper.PermissionMapper;
import com.example.backend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MenuService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    /**
     *
     * @description:更新用户授权时
     */
    public int updateMenu(int userId,int adminId,String permissions){
        try{
            String role = userMapper.findByUserId(adminId).getRole();
            System.out.println(role);
            if(role.equals("admin")||role.equals("manager")) {
                return permissionMapper.updatePermission(userId, permissions);
            }
            else{
                return 0;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }
    /**
     *
     * @description:新建用户时使用
     */
    public int insertMenu(int userId,String permissions){
        try{
                return permissionMapper.insertPermission(userId, permissions);
        }
        catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }
}
