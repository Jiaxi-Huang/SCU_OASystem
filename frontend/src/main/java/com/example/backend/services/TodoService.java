package com.example.backend.services;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.entity.ResponseBase;
import com.example.backend.entity.meeting.MeetingWithAdderId;
import com.example.backend.entity.todoList.TodoRecord;
import com.example.backend.entity.todoList.TodoRecordWithTk;
import com.example.backend.mapper.TodoListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class TodoService extends ServiceImpl<TodoListMapper, TodoRecord> {

    @Autowired
    private TodoListMapper todoListMapper;

    public List<TodoRecord> getAllRecords() {
        return  todoListMapper.getAll();
    }

    public List<TodoRecord> getRecordsByUserId(int user_id) {
        return  todoListMapper.getRecordsByUserId(user_id);
    }

    public List<TodoRecord> searchByAnything(String field, String key, int user_id) {
        return todoListMapper.searchByFieldAndKeyPersonal(field, key, user_id);
    }

    public int updateTodoRecord(TodoRecord record) {
//        System.out.println(record.getTodo_fin());
        int res_code = todoListMapper.updateTodoRecord(
                record.getUser_id(), record.getTodo_id(), record.getAdder_id(), record.getTodo_title(),
                record.getTodo_ctnt(), record.getTodo_fin(), record.getTodo_crt(), record.getTodo_ddl());
        return res_code;
    }

    public int insertTodoRecord(TodoRecordWithTk record) {
//        System.out.println(record.getTodo_fin());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        String formattedNow = now.format(formatter);
        int res_code = todoListMapper.insertTodoRecord(
                record.getUser_id(), record.getTodo_id(), record.getAdder_id(), record.getTodo_title(),
                record.getTodo_ctnt(), record.getTodo_fin(), formattedNow, record.getTodo_ddl());
        return res_code;
    }


    public int deleteRecord(TodoRecord record) {
//        System.out.println(record.getTodo_fin());
        int res_code = todoListMapper.deleteRecord(record.getTodo_id());
        return res_code;
    }

    public Integer getTotal() {
        return todoListMapper.getTotal();
    }

    public Integer getUnfinSelf() {
        return todoListMapper.getUnfinSelf();
    }

    public Integer getFinishedSelf() {
        return todoListMapper.getFinishedSelf();
    }

    public Integer getFinishedMana() {
        return todoListMapper.getFinishedMana();
    }

    public Integer getUnfinMana() {
        return todoListMapper.getUnfinMana();
    }
}
