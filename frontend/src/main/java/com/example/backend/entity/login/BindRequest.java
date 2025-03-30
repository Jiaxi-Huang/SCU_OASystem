package com.example.backend.entity.login;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BindRequest {
    private String openid;
    private String sessionKey;
    private String encryptedData;
    private String iv;
}
