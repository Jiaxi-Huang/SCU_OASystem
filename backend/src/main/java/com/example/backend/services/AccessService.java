package com.example.backend.services;

import com.example.backend.annotation.LogOperationWithId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
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
    public Integer getAuthenticatedId(String accessToken) {
        return redisTemplate.opsForValue().get(accessToken);
    }
    public Integer getOnlineUsers(){
        Set<Integer> userIds = new HashSet<>();
        // 使用 scan 迭代所有匹配模式的键
        try (Cursor<String> cursor = redisTemplate.scan(ScanOptions.scanOptions().match("*").count(1000).build())) {
            while (cursor.hasNext()) {
                String key = cursor.next();
                Integer userId = redisTemplate.opsForValue().get(key);
                if (userId != null) {
                    userIds.add(userId);
                }
            }
        }

        return userIds.size();
    }
}
