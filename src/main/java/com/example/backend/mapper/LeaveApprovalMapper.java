package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.leave.LeaveApprovalRecord;
import com.example.backend.entity.leave.LeaveJoinNotifyRecord;
import com.example.backend.entity.reimbursement.ReimJoinNotifyRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LeaveApprovalMapper extends BaseMapper<LeaveApprovalRecord> {
    @Select("""
        SELECT lr.leave_id,
               lr.user_id,
               lr.review_user_id,
               lr.start_date,
               lr.end_date,
               lr.type,
               lr.reason,
               lr.status,
               lr.submitted_at,
               nc.notification_id,
               nc.cc_user_id,
               nc.notified_user_id,
               nc.request_id,
               nc.request_type,
               nc.notified_at
        FROM leave_requests lr
        JOIN notification_chain nc ON lr.leave_id = nc.request_id
        WHERE nc.notified_user_id = #{userId} AND nc.request_type = 'leave'
       """)
    List<LeaveJoinNotifyRecord> getNotifyLeaveRecord(int userId);

    @Select("SELECT * FROM leave_requests")
    List<LeaveApprovalRecord> getAll();

    @Select("SELECT * FROM leave_requests WHERE user_id = #{userId}")
    List<LeaveApprovalRecord> getMyLeaveRecord(int userId);

    @Select("SELECT * FROM leave_requests WHERE review_user_id = #{userId}")
    List<LeaveApprovalRecord> getReviewLeaveRecord(int userId);

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
