package com.example.backend.entity.todoList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class TodoRecordWithTk {
    private int user_id;
    private int todo_id;
    private int adder_id;
    private String todo_title;
    private String todo_ctnt;
    private String todo_fin;
    private String todo_crt;
    private String todo_ddl;
    private String acsTkn;

    public TodoRecordWithTk(int todo_id, int user_id, int adder_id, String todo_title,
                            String todo_ctnt, String todo_fin, String todo_ddl, String todo_crt, String acsTkn) {
        this.todo_id = todo_id;
        this.user_id = user_id;
        this.adder_id = adder_id;
        this.todo_title = todo_title;
        this.todo_ctnt = todo_ctnt;
        this.todo_fin = todo_fin;
        this.todo_ddl = todo_ddl;
        this.todo_crt = todo_crt;
        this.acsTkn = acsTkn;
    }

}
