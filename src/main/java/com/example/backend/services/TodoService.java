package com.example.backend.services;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.entity.TodoRecord;
import com.example.backend.mapper.TodoListMapper;
import com.example.backend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.backend.entity.ReponseBase;

import java.util.List;

@Service
public class TodoService extends ServiceImpl<TodoListMapper, TodoRecord> {

    @Autowired
    private TodoListMapper todoListMapper;

    public List<TodoRecord> getAllRecords() {
        return  todoListMapper.getAll();
    }
}
