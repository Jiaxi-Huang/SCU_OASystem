package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("user_department_infos")
public class UserDepartment {
    private int user_id;
    private int department_id;
    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    public int getDepartmentId() {
        return department_id;
    }

    public void setDepartmentId(int department_id) {
        this.department_id = department_id;
    }

}
