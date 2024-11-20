package com.example.backend.controllers;

import com.example.backend.entity.User;
import com.example.backend.entity.userInfo.adminUserInfoRequest;
import com.example.backend.entity.userInfo.adminUserInfoResponse;
import com.example.backend.services.AccessService;
import com.example.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class Admin {
    @Autowired
    private UserService userService;
    @Autowired
    private AccessService accessService;
    @PostMapping("/user/list")
    public ResponseEntity<adminUserInfoResponse> userList(@RequestBody adminUserInfoRequest request) {
        try {
            String accessToken = request.getAccessToken();
            int userId = accessService.getAuthenticatedId(accessToken);
            List<User> userInfo = userService.adminUserInfo(userId);
            List<adminUserInfoResponse.Data> data = new ArrayList<>();
            for(User user:userInfo){
                adminUserInfoResponse.Data temp = new adminUserInfoResponse.Data();
                temp.setUserName(user.getUsername());
                temp.setUserDepartment(user.getDepartment());
                temp.setUserRole(user.getRole());
                temp.setUserPhone(user.getPhone());
                data.add(temp);
            }
            adminUserInfoResponse response = new adminUserInfoResponse(
                    0,
                    "获取用户列表成功",
                    true,
                    data
            );
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (Exception e) {
            adminUserInfoResponse response = new adminUserInfoResponse(
                    1,
                    "服务器内部错误",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
