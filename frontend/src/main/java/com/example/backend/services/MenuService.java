package com.example.backend.services;

import com.example.backend.annotation.LogOperation;
import com.example.backend.annotation.LogOperationWithId;
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
    @LogOperationWithId(value="修改用户权限",idParamIndex=1)
    public int updateMenu(int userId,int adminId,String permissions){
        try{
            String role = userMapper.findByUserId(adminId).getRole();
            if(role.equals("admin")||role.equals("manager")) {
                if(permissionMapper.findPermission(userId)!=null) {
                    return permissionMapper.updatePermission(userId, permissions);
                }
                else{
                    return permissionMapper.insertPermission(userId, permissions);
                }
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
    @LogOperation("初始化用户权限")
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
