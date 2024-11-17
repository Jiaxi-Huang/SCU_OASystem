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
}
