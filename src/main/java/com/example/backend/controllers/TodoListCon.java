package com.example.backend.controllers;

import com.example.backend.entity.todoList.TodoRecord;
import com.example.backend.entity.todoList.TodoRecordWithTk;
import com.example.backend.entity.ResponseBase;
import com.example.backend.entity.userInfo.adminUserInfoRequest;
import com.example.backend.services.AccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.backend.services.TodoService;
import java.util.List;

@RestController
@RequestMapping("/api/todolist")
public class TodoListCon {

    @Autowired
    private TodoService my_service;

    @Autowired
    private AccessService accessService;

    @PostMapping("/getRec")
    public ResponseBase getRec(@RequestBody adminUserInfoRequest request) {
//        System.out.println("[getRec] receive");
        ResponseBase res = new ResponseBase();

        try {
            String accessToken = request.getAccessToken();
            int userId = accessService.getAuthenticatedId(accessToken);
            List<TodoRecord> records = my_service.getRecordsByUserId(userId);

            for (TodoRecord record : records) {
                res.pushData(record);
            }
        }

        catch (Exception e) {
            res.setStatus(-1);
            res.setMessage(e.getMessage());
        }
        return res;
    }

    @PostMapping("/modifyRec")
    public ResponseBase modifyRec(@RequestBody TodoRecord record) {
        int res_code = my_service.updateTodoRecord(record);
//        System.out.println("modifyRec res_code: " + res_code);
        return new ResponseBase();
    }

    @PostMapping("/add")
    public ResponseBase addRec(@RequestBody TodoRecordWithTk request) {
        ResponseBase res = new ResponseBase();
        int res_code = -1;
        try {
            String accessToken = request.getAcsTkn();
            System.out.println(accessToken);
            int userId = accessService.getAuthenticatedId(accessToken);
            request.setAdder_id(userId);
            request.setUser_id(userId);
            res_code = my_service.insertTodoRecord(request);
        } catch (Exception e) {
            res.setStatus(-1);
            res.setMessage(e.getMessage());
        }
        System.out.println("addRec res_code: " + res_code);
        return res;
    }


    @PostMapping("/deleteTodo")
    public ResponseBase deleteRec(@RequestBody TodoRecord record) {
        System.out.println("deleteRecord " + record.getTodo_id());
        int res_code = my_service.deleteRecord(record);
        System.out.println("deleteRecord res_code: " + res_code);
        return new ResponseBase();
    }
}
