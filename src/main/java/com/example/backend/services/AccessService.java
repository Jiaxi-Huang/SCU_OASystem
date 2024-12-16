package com.example.backend.services;

import com.example.backend.annotation.LogOperation;
import com.example.backend.annotation.LogOperationWithId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.TimeUnit;
@Service
public class AccessService {
    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;

    public String generateAccessToken(int length) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
    @LogOperationWithId(value="生成Token",idParamIndex = 0)
    public boolean storeAccessToken(int authenticatedId,String accessToken) {
        redisTemplate.opsForValue().set(accessToken,authenticatedId, 24, TimeUnit.HOURS);
        return true;
    }
    @LogOperation("Token获取ID")
    public Integer getAuthenticatedId(String accessToken) {
        return redisTemplate.opsForValue().get(accessToken);
    }
}
