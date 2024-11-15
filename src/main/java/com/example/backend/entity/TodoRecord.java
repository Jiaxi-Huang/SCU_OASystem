package com.example.backend.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodoRecord {
    private int id;
    private String title;
    private String content;
    private int user_id;

    public TodoRecord(int id, String title, String content, int user_id) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.user_id = user_id;
    }

}
