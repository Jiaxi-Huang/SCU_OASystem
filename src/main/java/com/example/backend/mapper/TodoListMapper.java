package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.TodoRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface TodoListMapper extends BaseMapper<TodoRecord> {
    @Select("SELECT * FROM usertodo")
    List<TodoRecord> getAll();
}
