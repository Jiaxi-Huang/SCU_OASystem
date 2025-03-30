package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.NotifyRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NotifyMapper extends BaseMapper<NotifyRecord> {

    @Insert("INSERT INTO notification_chain (notified_user_id, request_type, request_id, notified_at, user_id"+
    " VALUES (#{notifiedUserId}, #{requestType}, #{requestId}, #{notifiedAt}, #{user_id})")
    int addNotification(int notifiedUserId, String requestType, int requestId, String notifiedAt, int userId);
}
