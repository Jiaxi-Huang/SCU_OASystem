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


    @Update("UPDATE usermeetings set adder_id = #{adder_id} where user_id = #{user_id} and mtin_id = #{mtin_id}")
    void updateMeetingAdder(int user_id, int adder_id, int mtin_id);


    @Update("UPDATE meetings set mtin_title = #{mtin_title}, mtin_ctnt = #{mtin_ctnt}, mtin_fin = #{mtin_fin}, " +
            "mtin_st = #{mtin_st}, mtin_len = #{mtin_len}, mtin_crt = #{mtin_crt}, mtin_host = #{mtin_host}, " +
            "mtin_loc = #{mtin_loc} " +
            "where mtin_id = #{mtin_id}")
    void updateMeeting(int mtin_id, String mtin_title, String mtin_ctnt, int mtin_fin, String mtin_st,
                       String mtin_len, String mtin_crt, String mtin_host, String mtin_loc);

    @Insert("INSERT INTO meetings(mtin_title, mtin_ctnt, " +
            "mtin_fin, mtin_st, mtin_len, mtin_host, mtin_loc, mtin_crt) VALUES " +
            "(#{mtin_title}, #{mtin_ctnt}, #{mtin_fin}, #{mtin_st}, #{mtin_len}, #{mtin_host}, " +
            "#{mtin_loc}, #{mtin_crt})")
    @Options(useGeneratedKeys = true, keyProperty = "mtin_id")
    void createMeeting(Meeting meeting);

    @Insert("INSERT INTO usermeetings(user_id, mtin_id, adder_id) VALUES (#{user_id}, #{meeting_id}, #{adder_id})")
    void addMeetingForSomeone(int user_id, int meeting_id, int adder_id);

}
