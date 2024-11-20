package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.LeaveApprovalRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LeaveApprovalMapper extends BaseMapper<LeaveApprovalRecord> {

    @Select("SELECT * FROM leave_requests")
    List<LeaveApprovalRecord> getAll();

    @Update("UPDATE leave_requests " +
            "SET start_date = #{start_date}, end_date = #{end_date}, " +
            "reason = #{reason}, status = #{status}, submitted_at = #{submitted_at} " +
            "WHERE user_id = #{user_id} AND leave_id = #{leave_id}")
    int updateLeaveApprovalRecord(int leave_id, int user_id, String start_date, String end_date,
                                  String reason, String status, String submitted_at);

    @Insert("INSERT INTO leave_requests (leave_id, user_id, start_date, end_date, reason, status, submitted_at)" +
            " VALUES(#{leave_id}, #{user_id}, #{start_date}, #{end_date}, #{reason}, #{status}, #{submitted_at})")
    int insertLeaveApprovalRecord(int leave_id, int user_id, String start_date, String end_date,
                                 String reason, String status, String submitted_at);

    @Delete("DELETE FROM leave_requests WHERE leave_id = #{leave_id}")
    int deleteRecord(int leave_id);
}
