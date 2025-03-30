package com.example.backend.services;

import com.example.backend.annotation.LogOperation;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class CaptchaService {
    @Resource
    private JavaMailSender mailSender;
    private String from ="203712538@qq.com";

    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;
    /**
     * 发送纯文本的邮件
     * @param to 发收件人
     * @param subject 主题
     * @param content 内容
     * @return 是否成功
     */
    @LogOperation("发送验证码")
    @SneakyThrows(Exception.class)
    public boolean sendEmail(String to, String subject, String content,int captcha) {
        try {
            //连接内容
            content = content + captcha;
            // 创建邮件消息
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            // 设置收件人
            message.setTo(to);
            // 设置邮件主题
            message.setSubject(subject);
            // 设置邮件内容
            message.setText(content);
            // 发送邮件
            mailSender.send(message);
            //存入redis缓存，90s内有效
            redisTemplate.opsForValue().set(to, captcha, 90, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            System.out.println("邮件发送失败: " + e);
            return false;
        }
    }
    @LogOperation("校验验证码")
    public int verifyCaptcha(String email, int captcha) {
        // 从 Redis 中获取验证码
        Integer storedCaptcha = redisTemplate.opsForValue().get(email);
        if (storedCaptcha == null) {
            return 2; // 验证码不存在或已过期
        }
        else if(storedCaptcha != captcha){
            return 1;//不正确
        }
        else{
            return 0;
        }
    }
}
