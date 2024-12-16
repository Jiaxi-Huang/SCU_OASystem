package com.example.backend.services;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.annotation.LogOperationWithId;
import com.example.backend.entity.Avatar;
import com.example.backend.mapper.UploadMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UploadService extends ServiceImpl<UploadMapper, Avatar> {

    @Autowired
    private UploadMapper uploadMapper;
    @LogOperationWithId(value="用户修改头像",idParamIndex=0)
    public int uploadPersonalAvatar(int user_id,String url) {
        try {
            Avatar avatar = uploadMapper.findAvatarByUserId(user_id);
            if (avatar != null) {
                return uploadMapper.updatePersonalAvatar(user_id, url);
            }
            else {
                return uploadMapper.insertPersonalAvatar(user_id, url);
            }
        } catch (Exception e) {
            // 记录异常信息
            e.printStackTrace();
            // 可以选择返回一个特定的错误码或抛出自定义异常
            return -1; // 表示重置密码失败
        }
    }
}
