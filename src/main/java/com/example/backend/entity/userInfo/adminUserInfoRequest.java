package com.example.backend.entity.userInfo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class adminUserInfoRequest {
    private int userId;
    private String accessToken;
    private String userName;
    private String userRole;
    private String userDepartment;
    private List<Integer> user_ids;
}
