package com.example.backend.entity.todoList;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@TableName("usertodo")
public class TodoRecordWithMultiUsers {

    private String todo_title;
    private String todo_ctnt;
    private String todo_ddl;
    private ArrayList<Integer> user_ids;
    private String accessToken;
    private String todo_fin;

    public TodoRecordWithMultiUsers(String todo_title, String todo_ctnt, String todo_ddl,
                                    ArrayList<Integer> user_ids, String accessToken, String todo_fin) {
        this.todo_title = todo_title;
        this.todo_ctnt = todo_ctnt;
        this.todo_ddl = todo_ddl;
        this.user_ids = user_ids;
        this.accessToken = accessToken;
        this.todo_fin = todo_fin;
    }
}
