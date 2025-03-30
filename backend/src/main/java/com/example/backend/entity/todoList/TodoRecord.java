package com.example.backend.entity.todoList;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("usertodo")
public class TodoRecord {

    private int user_id;
    private int todo_id;
    private int adder_id;
    private String todo_title;
    private String todo_ctnt;
    private String todo_fin;
    private String todo_crt;
    private String todo_ddl;

    public TodoRecord(int user_id, int todo_id, int adder_id, String todo_title,
                      String todo_ctnt, String todo_fin, String todo_crt, String todo_ddl) {
        this.todo_id = todo_id;
        this.user_id = user_id;
        this.adder_id = adder_id;
        this.todo_title = todo_title;
        this.todo_fin = todo_fin;
        this.todo_crt = todo_crt;
        this.todo_ddl = todo_ddl;
        this.todo_ctnt = todo_ctnt;
    }

}
