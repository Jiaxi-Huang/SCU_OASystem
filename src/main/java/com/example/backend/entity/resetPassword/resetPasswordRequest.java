package com.example.backend.entity.resetPassword;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class resetPasswordRequest {
    private String email;
    private String password;
    private int captcha;
}

