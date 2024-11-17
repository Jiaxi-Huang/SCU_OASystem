package com.example.backend.controllers;

import com.example.backend.entity.userInfo.personalSettingRequest;
import com.example.backend.entity.userInfo.personalSettingResponse;
import com.example.backend.services.AccessService;
import com.example.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/setting")
public class Personal {
    @Autowired
    private UserService userService;
    @Autowired
    private AccessService accessService;

    @PostMapping("/basicInfo")
    public ResponseEntity<personalSettingResponse> basicInfo(@RequestBody personalSettingRequest request) {
        try{
        String username = request.getUsername();
        String phone = request.getPhone();
        String intro = request.getIntro();
        String accessToken = request.getAccessToken();
        int userId = accessService.getAuthenticatedId(accessToken);
        int isSuccess = userService.basicInfoSetting(username,phone,intro,userId);
        if(isSuccess > 0){
            personalSettingResponse.Data data = new personalSettingResponse.Data(username,phone,intro);
            personalSettingResponse response = new personalSettingResponse(
                    0,
                    "更新成功",
                    true,
                    data
            );
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
            else{
                personalSettingResponse response = new personalSettingResponse(
                        0,
                        "更新失败",
                        false,
                        null
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        }
        catch(Exception e){
            personalSettingResponse response = new personalSettingResponse(
                    2,
                    "服务器内部错误",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
