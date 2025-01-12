package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.Department;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DepartmentMapper extends BaseMapper<Department>{
    @Select("SELECT * FROM department_infos")
    List<Department> findAllDepartment();
    @Insert("INSERT INTO department_infos (department_name) VALUES(#{department_name})")
    int adminInsertDepartment(String department_name);
    @Delete("DELETE FROM department_infos WHERE department_id=#{department_id}")
    int adminDeleteDepartment(int department_id);
}
