package com.example.backend.entity.log;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LogRequest {
    private String accessToken;
    private List<Integer> ids;
}
