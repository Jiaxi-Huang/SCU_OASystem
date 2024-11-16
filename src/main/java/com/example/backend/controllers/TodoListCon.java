package com.example.backend.controllers;

import com.example.backend.entity.TodoRecord;
import com.example.backend.entity.ReponseBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.backend.mapper.TodoListMapper;
import com.example.backend.services.TodoService;
import java.util.List;

@RestController
@RequestMapping("/api/todolist")
public class TodoListCon {

    @Autowired
    private TodoService my_service;

    @PostMapping("/getRec")
    public ReponseBase getRec() {
//        System.out.println("[getRec] receive");
        ReponseBase res = new ReponseBase();
        List<TodoRecord> records = my_service.getAllRecords();

        for (TodoRecord record : records) {
            res.pushData(record);
        }
        return res;
    }

    @PostMapping("/modifyRec")
    public ReponseBase modifyRec(@RequestBody TodoRecord record) {
        int res_code = my_service.updateTodoRecord(record);
//        System.out.println("modifyRec res_code: " + res_code);
        return new ReponseBase();
    }

    @PostMapping("/add")
    public ReponseBase addRec(@RequestBody TodoRecord record) {
        int res_code = my_service.insertTodoRecord(record);
        System.out.println("addRec res_code: " + res_code);
        return new ReponseBase();
    }


    @PostMapping("/deleteTodo")
    public ReponseBase deleteRec(@RequestBody TodoRecord record) {
        System.out.println("deleteRecord " + record.getTodo_id());
        int res_code = my_service.deleteRecord(record);
        System.out.println("deleteRecord res_code: " + res_code);
        return new ReponseBase();
    }
}
