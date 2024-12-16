package com.example.backend.controllers;

import com.example.backend.entity.OperationLog;
import com.example.backend.entity.log.LogRequest;
import com.example.backend.entity.log.LogResponse;
import com.example.backend.services.AccessService;
import com.example.backend.services.OperationLogService;
import com.example.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/log")
public class OperationLogController {
    @Autowired
    private UserService userService;
    @Autowired
    private AccessService accessService;
    @Autowired
    private OperationLogService logService;
    @RequestMapping("/list")
    public ResponseEntity<LogResponse> userList(@RequestBody LogRequest request) {
        try {
            String accessToken = request.getAccessToken();
            int userId = accessService.getAuthenticatedId(accessToken);
            String role = userService.getById(userId).getRole();
            if(role.equals("admin")) {
                List<OperationLog> logs = logService.logList(userId);
                List<LogResponse.Data> data = new ArrayList<>();
                for (OperationLog log : logs) {
                    LogResponse.Data temp = new LogResponse.Data();
                    temp.setId(log.getId());
                    temp.setUserId(log.getUser_id());
                    temp.setLogContent(log.getContent());
                    temp.setLogDate(log.getDate());
                    data.add(temp);
                }
                LogResponse response = new LogResponse(
                        0,
                        "获取操作日志成功",
                        true,
                        data
                );
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            else{
                LogResponse response = new LogResponse(
                        1,
                        "权限不足",
                        false,
                        null
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        }
        catch (Exception e){
            LogResponse response = new LogResponse(
                    2,
                    "服务器内部错误",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @RequestMapping("/delete")
    public ResponseEntity<LogResponse> userDelete(@RequestBody LogRequest request) {
        try {
            String accessToken = request.getAccessToken();
            List<Integer> ids = request.getIds();
            int userId = accessService.getAuthenticatedId(accessToken);
            String role = userService.getById(userId).getRole();
            if(role.equals("admin")) {
                int isSuccess = logService.logDelete(userId,ids);
                if(isSuccess>0) {
                    LogResponse response = new LogResponse(
                            0,
                            "获取操作日志成功",
                            true,
                            null
                    );
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
                else{
                    LogResponse response = new LogResponse(
                            0,
                            "获取操作日志失败",
                            true,
                            null
                    );
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
            }
            else{
                LogResponse response = new LogResponse(
                        1,
                        "权限不足",
                        false,
                        null
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        }
        catch (Exception e){
            LogResponse response = new LogResponse(
                    2,
                    "服务器内部错误",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
