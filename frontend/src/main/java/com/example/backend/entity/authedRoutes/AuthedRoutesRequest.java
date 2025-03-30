package com.example.backend.entity.authedRoutes;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AuthedRoutesRequest {
    private String roleName;
    private String accessToken;
    private int userId;
    private List<String> authedRoutes;
}
