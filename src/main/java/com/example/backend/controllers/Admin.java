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
                temp.setUserId(user.getUserId());
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
    @PostMapping("/user/add")
    public ResponseEntity<adminUserInfoResponse> userAdd(@RequestBody adminUserInfoRequest request) {
        try {
            String accessToken = request.getAccessToken();
            int adminId = accessService.getAuthenticatedId(accessToken);
            String username = request.getUserName();
            String userdepartment = request.getUserDepartment();
            String userrole = request.getUserRole();
            int isSuccess = userService.adminUserAdd(username, userdepartment, userrole, adminId);
            if (isSuccess > 0) {
                adminUserInfoResponse response = new adminUserInfoResponse(
                        0,
                        "获取用户列表成功",
                        true,
                        null
                );
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            else{
                adminUserInfoResponse response = new adminUserInfoResponse(
                        1,
                        "添加用户失败",
                        false,
                        null
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
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
    @PostMapping("/user/update")
    public ResponseEntity<adminUserInfoResponse> userUpdate(@RequestBody adminUserInfoRequest request) {
        try {
            String accessToken = request.getAccessToken();
            String username = request.getUserName();
            String userdepartment = request.getUserDepartment();
            String userrole = request.getUserRole();
            int userId = request.getUserId();
            int adminId = accessService.getAuthenticatedId(accessToken);
            int isSuccess = userService.adminUserUpdate(username, userdepartment, userrole, adminId, userId);
            if (isSuccess > 0) {
                adminUserInfoResponse response = new adminUserInfoResponse(
                        0,
                        "更新用户信息成功",
                        true,
                        null
                );
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            else{
                adminUserInfoResponse response = new adminUserInfoResponse(
                        1,
                        "更新用户信息失败",
                        false,
                        null
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        }
        catch (Exception e) {
            adminUserInfoResponse response = new adminUserInfoResponse(
                    2,
                    "服务器内部错误",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/user/delete")
    public ResponseEntity<adminUserInfoResponse> userDelete(@RequestBody adminUserInfoRequest request) {
        try {
            String accessToken = request.getAccessToken();
            int userId = request.getUserId();
            int adminId = accessService.getAuthenticatedId(accessToken);
            int isSuccess = userService.adminUserDelete(adminId, userId);
            if (isSuccess > 0) {
                adminUserInfoResponse response = new adminUserInfoResponse(
                        0,
                        "获取用户列表成功",
                        true,
                        null
                );
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            else{
                adminUserInfoResponse response = new adminUserInfoResponse(
                        1,
                        "删除用户失败",
                        false,
                        null
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        }
        catch (Exception e) {
            adminUserInfoResponse response = new adminUserInfoResponse(
                    2,
                    "服务器内部错误",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
