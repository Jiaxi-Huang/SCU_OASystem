package com.example.backend.entity.meeting;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class FieldKeyWithToken {
    private String field;
    private String key;
    private String accessToken;

    public FieldKeyWithToken(String field, String key, String accessToken) {
        this.field = field;
        this.key = key;
        this.accessToken = accessToken;
    }
}
