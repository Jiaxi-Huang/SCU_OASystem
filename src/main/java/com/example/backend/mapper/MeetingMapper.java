package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.meeting.Meeting;
import com.example.backend.entity.meeting.MeetingWithAdderId;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface MeetingMapper extends BaseMapper<MeetingWithAdderId> {

    @Select("SELECT mtin_id, adder_id, mtin_title, mtin_ctnt, " +
            "mtin_fin, mtin_st, mtin_len, mtin_host, mtin_loc, mtin_crt FROM meetings natural join usermeetings WHERE user_id = #{user_id}")
    List<MeetingWithAdderId> getPersonalMeetings(int user_id);

}
