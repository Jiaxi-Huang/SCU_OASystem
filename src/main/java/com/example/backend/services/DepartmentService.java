package com.example.backend.services;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.annotation.LogOperationWithId;
import com.example.backend.entity.Department;
import com.example.backend.entity.User;
import com.example.backend.mapper.DepartmentMapper;
import com.example.backend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService extends ServiceImpl<DepartmentMapper, Department> {
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private UserMapper userMapper;
    @LogOperationWithId(value="请求部门列表",idParamIndex=0)
    public List<Department> adminDepartmentInfo(int user_id){
        try {
            User user = userMapper.findByUserId(user_id);
            String role = user.getRole();
            if (role.equals("admin")) {
                return departmentMapper.findAllDepartment();
            }
            return null;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @LogOperationWithId(value="新增部门",idParamIndex=0)
    public int adminDepartmentAdd(int user_id,String departmentName){
        try {
            User user = userMapper.findByUserId(user_id);
            String role = user.getRole();
            if (role.equals("admin")) {
                return departmentMapper.adminInsertDepartment(departmentName);
            }
            return 0;
        }
        catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }
    @LogOperationWithId(value="删除部门",idParamIndex=0)
    public int adminDepartmentDelete(int user_id,int departmentId){
        try {
            User user = userMapper.findByUserId(user_id);
            String role = user.getRole();
            if (role.equals("admin")) {
                return departmentMapper.adminDeleteDepartment(departmentId);
            }
            return 0;
        }
        catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

}
