package com.example.backend.entity;
// GUO YUAN created

// LOMBOK is necessary here
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class ResponseBase {
    protected int status;
    protected String message;
    protected ArrayList<Object> data;

    public ResponseBase() {
        this.status = 0;
        this.message = "No override Msg";
        this.data = new ArrayList<>();
    }

    public void pushData(Object record) {
        this.data.add(record);
    }
}
