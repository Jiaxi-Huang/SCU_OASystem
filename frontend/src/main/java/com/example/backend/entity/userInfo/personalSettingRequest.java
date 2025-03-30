package com.example.backend.entity.userInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class personalSettingRequest {
    private String username;
    private String phone;
    private String intro;
    private String accessToken;
    private String newEmail;
    private String oldEmail;
    private String oldPassword;
    private String newPassword;
    private int captcha;
}
