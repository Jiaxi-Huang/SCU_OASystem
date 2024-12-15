package com.example.backend.controllers;

import cn.hutool.json.JSONObject;
import com.example.backend.entity.User;
import com.example.backend.entity.authedRoutes.AuthedRoutesRequest;
import com.example.backend.entity.authedRoutes.AuthedRoutesResponse;
import com.example.backend.entity.captcha.CaptchaRequest;
import com.example.backend.entity.captcha.CaptchaResponse;
import com.example.backend.entity.login.BindRequest;
import com.example.backend.entity.login.LoginRequest;
import com.example.backend.entity.login.LoginResponse;
import com.example.backend.entity.register.RegisterRequest;
import com.example.backend.entity.register.RegisterResponse;
import com.example.backend.entity.resetPassword.resetPasswordRequest;
import com.example.backend.entity.resetPassword.resetPasswordResponse;
import com.example.backend.entity.userInfo.userInfoRequest;
import com.example.backend.entity.userInfo.userInfoResponse;
import com.example.backend.services.AccessService;
import com.example.backend.services.CaptchaService;
import com.example.backend.services.UserService;
import com.example.backend.services.utils.DecryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/auth")
public class Auth {

    @Autowired
    private UserService userService;
    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private AccessService accessService;
    @PostMapping("/user/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            //获取登录信息
            String email = request.getEmail();
            String password = request.getPassword();
            int authenticatedId = userService.login(email, password);
            if (authenticatedId>0) {//这里获取的ID必须大于0
                String accessToken = accessService.generateAccessToken(32);
                boolean isStored = accessService.storeAccessToken(authenticatedId,accessToken);
                if(isStored) {
                    LoginResponse response = new LoginResponse(
                            0,
                            "登录成功",
                            true,
                            accessToken
                    );
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
                else{
                    LoginResponse response = new LoginResponse(
                            0,
                            "Token生成失败",
                            false,
                            null
                    );
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
            } else {
                // 构建失败响应
                LoginResponse response = new LoginResponse(
                        1,
                        "无效的用户名或密码",
                        false,
                        null
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            LoginResponse response = new LoginResponse(
                    2,
                    "服务器内部错误",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/user/userInfo")
    public ResponseEntity<userInfoResponse> userInfo(@RequestBody userInfoRequest request) {
        try {
            //获取邮箱
            String email = request.getEmail();
            User userInfo = userService.userInfo(email);
            String avatar = userService.userInfoAvatar(email);
            if (userInfo!=null) {
                userInfoResponse.Data userInfoResponseData = new userInfoResponse.Data();
                userInfoResponseData.setRoleName(userInfo.getRole());
                userInfoResponseData.setUserName(userInfo.getUsername());
                userInfoResponseData.setUserIntro(userInfo.getIntro());
                userInfoResponseData.setUserPhone(userInfo.getPhone());
                userInfoResponseData.setUserDepartment(userInfo.getDepartment());
                userInfoResponseData.setUserAvatar(avatar);
                userInfoResponse response = new userInfoResponse(
                        0,
                        "获取职位信息成功",
                        true,
                        userInfoResponseData
                );
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                // 构建失败响应
                userInfoResponse response = new userInfoResponse(
                        1,
                        "获取职位信息失败",
                        false,
                        null
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            userInfoResponse response = new userInfoResponse(
                    2,
                    "服务器内部错误",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/permission/routes")
    public ResponseEntity<AuthedRoutesResponse> autheredRoutes(@RequestBody AuthedRoutesRequest request) {
        //获取邮箱
        try{
            String roleName = request.getRoleName();
            if (roleName != null) {
                if (roleName.equals("admin")) {
                    AuthedRoutesResponse.Data data = new AuthedRoutesResponse.Data();
                    List<String> authedRoutes = Arrays.asList(
                            "/dashboard", "/guide", "/menu","/dragable", "/calendar", "/menu", "/upload", "/personal",
                            "/copy","/zip", "/excel", "/table", "/projectboard","/qrcode","/cropper", "/editor",
                            "/role","/worker","/leaveApproval","/reimbursement","/todoList", "/meetings","/file","/MyCalender"
                    );
                    data.setAuthedRoutes(authedRoutes);
                    AuthedRoutesResponse response = new AuthedRoutesResponse(
                            0,
                            "获取职位信息成功",
                            true,
                            data
                    );
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else if (roleName.equals("manager")) {
                    AuthedRoutesResponse.Data data = new AuthedRoutesResponse.Data();
                    List<String> authedRoutes = Arrays.asList(
                            "/worker", "/todoList", "/reimbursement","/meetings","/leaveApproval","/file","/MyCalender","/personal");
                    data.setAuthedRoutes(authedRoutes);
                    AuthedRoutesResponse response = new AuthedRoutesResponse(
                            0,
                            "获取职位信息成功",
                            true,
                            data
                    );
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    AuthedRoutesResponse.Data data = new AuthedRoutesResponse.Data();
                    List<String> authedRoutes = Arrays.asList(
                            "/meetings","/todoList","/leaveApproval","/reimbursement","/file", "/MyCalender","/personal");
                    data.setAuthedRoutes(authedRoutes);
                    AuthedRoutesResponse response = new AuthedRoutesResponse(
                            0,
                            "获取职位信息成功",
                            true,
                            data
                    );
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
            } else {
                // 构建失败响应
                AuthedRoutesResponse response = new AuthedRoutesResponse(
                        1,
                        "获取职位信息失败",
                        false,
                        null
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        }catch (Exception e) {
            AuthedRoutesResponse response = new AuthedRoutesResponse(
                    2,
                    "服务器内部错误",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/user/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        try {
            //获取邮箱
            String email = request.getEmail();
            String password = request.getPassword();
            //这里逻辑要改
            int captcha = request.getCaptcha();
            int isVerified = captchaService.verifyCaptcha(email, captcha);
            if (isVerified == 0) {
                int isRegistered = userService.register(email, password);
                if (isRegistered > 0) {
                    RegisterResponse response = new RegisterResponse(
                            0,
                            "注册成功",
                            true,
                            null
                    );
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    // 构建失败响应
                    RegisterResponse response = new RegisterResponse(
                            1,
                            "注册失败",
                            false,
                            null
                    );
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
                }
            }
            else if(isVerified == 1) {
                RegisterResponse response = new RegisterResponse(
                        1,
                        "验证码输入错误",
                        false,
                        null
                );
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            else{
                RegisterResponse response = new RegisterResponse(
                        2,
                        "验证码过期",
                        false,
                        null
                );
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        }catch (Exception e) {
            RegisterResponse response = new RegisterResponse(
                    2,
                    "服务器内部错误",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/email/sendCaptcha")
    public ResponseEntity<CaptchaResponse> SendCaptcha(@RequestBody CaptchaRequest request) {
        try {
            //获取邮箱
            String email = request.getEmail();
            //生成验证码存到会话或者缓存服务器
            String subject ="【XM07】OA办公系统";
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
    @PostMapping("/user/reset-password")
    public ResponseEntity<resetPasswordResponse> ResetPassword(@RequestBody resetPasswordRequest request) {
        try {
            //获取信息
            String email = request.getEmail();
            String password = request.getPassword();
            int captcha = request.getCaptcha();
            //验证验证码
            int isVerified = captchaService.verifyCaptcha(email, captcha);
            if (isVerified == 0) {
                int isReset = userService.resetPassword(email, password);
                if (isReset > 0) {
                    resetPasswordResponse response = new resetPasswordResponse(
                            0,
                            "重置密码成功",
                            true,
                            null
                    );
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    // 构建失败响应
                    resetPasswordResponse response = new resetPasswordResponse(
                            1,
                            "重置密码失败",
                            false,
                            null
                    );
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
                }
            }
            else if(isVerified == 1) {
                resetPasswordResponse response = new resetPasswordResponse(
                        1,
                        "验证码输入错误",
                        false,
                        null
                );
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            else{
                resetPasswordResponse response = new resetPasswordResponse(
                        2,
                        "验证码过期",
                        false,
                        null
                );
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        }catch (Exception e) {
            resetPasswordResponse response = new resetPasswordResponse(
                    2,
                    "服务器内部错误",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/wechat/login")
    public ResponseEntity<LoginResponse> loginWechat(@RequestBody LoginRequest request) {
        try {
            String openid = request.getOpenid();
            int isSuccess = userService.loginByWechat(openid);
            if (isSuccess > 0) {
                return ResponseEntity.ok(new LoginResponse(0, "登录成功", true, accessService.generateAccessToken(32)));
            } else {
                return ResponseEntity.ok(new LoginResponse(1, "登录失败（请先绑定已注册账号对应的邮箱）", false, null));
            }
        }
        catch(Exception e){
            return ResponseEntity.ok(new LoginResponse(2, "服务器内部错误", false, null));
        }
    }
    @PostMapping("/wechat/bind")
    public ResponseEntity<LoginResponse> bindWechat(@RequestBody BindRequest request) {
        try {
            String openid = request.getOpenid();
            String sessionKey = request.getSessionKey();
            String encryptedData = request.getEncryptedData();
            String iv = request.getIv();
            JSONObject decryptedData = DecryptUtil.decrypt(sessionKey, encryptedData, iv);
            String phone = decryptedData.getStr("phoneNumber");
            int isSuccess = userService.bindByWechat(openid,phone);
            if (isSuccess > 0) {
                return ResponseEntity.ok(new LoginResponse(0, "绑定成功", true, accessService.generateAccessToken(32)));
            } else {
                return ResponseEntity.ok(new LoginResponse(1, "绑定失败", false, null));
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.ok(new LoginResponse(2, "服务器内部错误", false, null));
        }
    }
}
