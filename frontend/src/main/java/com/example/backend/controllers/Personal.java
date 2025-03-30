package com.example.backend.controllers;

import com.example.backend.entity.ResponseBase;
import com.example.backend.entity.captcha.CaptchaRequest;
import com.example.backend.entity.captcha.CaptchaResponse;
import com.example.backend.entity.userInfo.UserEmailDTO;
import com.example.backend.entity.userInfo.personalSettingRequest;
import com.example.backend.entity.userInfo.personalSettingResponse;
import com.example.backend.services.AccessService;
import com.example.backend.services.CaptchaService;
import com.example.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/api/setting")
public class Personal {
    @Autowired
    private UserService userService;
    @Autowired
    private AccessService accessService;
    @Autowired
    private CaptchaService captchaService;
    @PostMapping("/wechatInfo")
    public ResponseEntity<personalSettingResponse> wechatInfo(@RequestBody personalSettingRequest request) {
        try{
        String username = request.getUsername();
        String intro = request.getIntro();
        String accessToken = request.getAccessToken();
        int userId = accessService.getAuthenticatedId(accessToken);
        int isSuccess = userService.wechatInfoSetting(username,intro,userId);
        if(isSuccess > 0){
            personalSettingResponse.Data data = new personalSettingResponse.Data(username,null,intro);
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
    @PostMapping("/resetEmail")
    ResponseEntity<ResponseBase> resetEmail(@RequestBody personalSettingRequest request){
        try{
            String oldEmail = request.getOldEmail();
            String newEmail = request.getNewEmail();
            int captcha = request.getCaptcha();
            String accessToken = request.getAccessToken();
            int user_id = accessService.getAuthenticatedId(accessToken);
            int isVerified = captchaService.verifyCaptcha(newEmail, captcha);
            if (isVerified == 0) {
                int isSuccess = userService.resetPersonalEmail(user_id, oldEmail, newEmail);
                if (isSuccess > 0) {
                    ResponseBase response = new ResponseBase();
                    response.setMessage("用户更新邮箱成功");
                    UserEmailDTO emailResponse= new UserEmailDTO();
                    emailResponse.setUserEmail(newEmail);
                    response.pushData(emailResponse);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    ResponseBase response = new ResponseBase();
                    response.setStatus(1);
                    response.setMessage("用户更新邮箱失败");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
                }
            }
            else if(isVerified == 1) {
                ResponseBase response = new ResponseBase();
                response.setStatus(1);
                response.setMessage("验证码输入错误");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            else{
                ResponseBase response = new ResponseBase();
                response.setStatus(1);
                response.setMessage("验证码过期");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        }
        catch(Exception e){
            ResponseBase response = new ResponseBase();
            response.setStatus(2);
            response.setMessage("服务器内部错误");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/resetPassword")
    ResponseEntity<ResponseBase> resetPassword(@RequestBody personalSettingRequest request){
        try {
            String accessToken = request.getAccessToken();
            String oldPassword = request.getOldPassword();
            String newPassword = request.getNewPassword();
            int user_id = accessService.getAuthenticatedId(accessToken);
            if (user_id >= 0) {
                int isSuccess = userService.resetPersonalPassword(user_id, oldPassword, newPassword);
                if (isSuccess > 0) {
                    ResponseBase response = new ResponseBase();
                    response.setMessage("用户更新密码成功");
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    ResponseBase response = new ResponseBase();
                    response.setStatus(1);
                    response.setMessage("用户更新密码失败");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
                }
            }
            else{
                ResponseBase response = new ResponseBase();
                response.setStatus(1);
                response.setMessage("用户登录状态错误");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        }
        catch(Exception e){
            ResponseBase response = new ResponseBase();
            response.setStatus(2);
            response.setMessage("服务器内部错误");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/email/sendCaptcha")
    public ResponseEntity<CaptchaResponse> SendCaptcha(@RequestBody CaptchaRequest request) {
        try {
            //获取邮箱
            String email = request.getEmail();
            //生成验证码存到会话或者缓存服务器
            String subject ="[XM-07]OA办公系统";
            String content ="（60s内有效）验证码：";
            int captcha = new Random().nextInt(1000000);
            //通过QQ邮箱发送给用户
            boolean isSend = captchaService.sendEmail(email,subject,content,captcha);
            if (isSend) {
                CaptchaResponse response = new CaptchaResponse(
                        0,
                        "验证码发送成功",
                        true,
                        null
                );
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                // 构建失败响应
                CaptchaResponse response = new CaptchaResponse(
                        1,
                        "验证码发送失败",
                        false,
                        null
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            CaptchaResponse response = new CaptchaResponse(
                    2,
                    "服务器内部错误",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
