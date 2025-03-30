package com.example.backend.entity.department;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class adminDepartmentInfoRequest {
    private String accessToken;
    private int departmentId;
    private String departmentName;
}
