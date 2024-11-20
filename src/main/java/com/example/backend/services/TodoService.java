package com.example.backend.services;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.entity.TodoRecord;
import com.example.backend.mapper.TodoListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService extends ServiceImpl<TodoListMapper, TodoRecord> {

    @Autowired
    private TodoListMapper todoListMapper;

    public List<TodoRecord> getAllRecords() {
        return  todoListMapper.getAll();
    }

    public int updateTodoRecord(TodoRecord record) {
//        System.out.println(record.getTodo_fin());
        int res_code = todoListMapper.updateTodoRecord(
                record.getUser_id(), record.getTodo_id(), record.getAdder_id(), record.getTodo_title(),
                record.getTodo_ctnt(), record.getTodo_fin(), record.getTodo_crt(), record.getTodo_ddl());
        return res_code;
    }

    public int insertTodoRecord(TodoRecord record) {
//        System.out.println(record.getTodo_fin());
        int res_code = todoListMapper.insertTodoRecord(
                record.getUser_id(), record.getTodo_id(), record.getAdder_id(), record.getTodo_title(),
                record.getTodo_ctnt(), record.getTodo_fin(), record.getTodo_crt(), record.getTodo_ddl());
        return res_code;
    }


    public int deleteRecord(TodoRecord record) {
//        System.out.println(record.getTodo_fin());
        int res_code = todoListMapper.deleteRecord(record.getTodo_id());
        return res_code;
    }
}
