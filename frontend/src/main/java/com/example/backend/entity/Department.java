package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("department_infos")
public class Department {
    @TableId(type = IdType.INPUT)
    private int department_id;
    private String department_name;
    public int getDepartmentId() {
        return department_id;
    }

    public void setDepartmentId(int department_id) {
        this.department_id = department_id;
    }
    public String getDepartmentName() {
        return department_name;
    }
    public void setDepartmentName(String department_name) {
        this.department_name = department_name;
    }
}

