package com.example.backend.services;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.entity.NotifyRecord;
import com.example.backend.mapper.NotifyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotifyService extends ServiceImpl<NotifyMapper, NotifyRecord> {

    @Autowired
    private NotifyMapper NotifyMapper;

    public int addNotification(NotifyRecord notifyRecord) {
        return NotifyMapper.addNotification(
                notifyRecord.getNotified_user_id(), notifyRecord.getRequest_type(),
                notifyRecord.getRequest_id(), notifyRecord.getNotified_at(), notifyRecord.getUser_id()
        );
    }
}
