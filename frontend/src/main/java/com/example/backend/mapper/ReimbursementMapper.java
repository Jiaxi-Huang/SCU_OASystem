package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.reimbursement.ReimJoinNotifyRecord;
import com.example.backend.entity.reimbursement.ReimbursementRecord;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;


@Mapper
public interface ReimbursementMapper extends BaseMapper<ReimbursementRecord> {

    @Select("""
            SELECT rr.reimbursement_id,
               rr.user_id,
               rr.amount,
               rr.description,
               rr.status,
               rr.submitted_at,
               ui.username
        FROM reimbursement_requests rr
        JOIN user_infos ui ON rr.user_id = ui.user_id
            """)
    List<ReimbursementRecord> getAll();

    @Select("""
        SELECT rr.reimbursement_id,
               rr.user_id,
               rr.amount,
               rr.description,
               rr.status,
               rr.submitted_at,
               ui.username
        FROM reimbursement_requests rr
        JOIN user_infos ui ON rr.user_id = ui.user_id
        WHERE rr.review_user_id = #{user_id}
        AND rr.user_id = #{user_id}
        """)
    List<ReimbursementRecord> getReimbursementRecordByUserId(int user_id);

    @Select("""
        SELECT rr.reimbursement_id,
               rr.user_id,
               rr.amount,
               rr.description,
               rr.status,
               rr.submitted_at,
               nc.notification_id,
               nc.notified_user_id,
               nc.request_type,
               nc.request_id,
               nc.notified_at,
               nc.cc_user_id,
               ui.username
        FROM reimbursement_requests rr
        JOIN notification_chain nc ON rr.reimbursement_id = nc.request_id
        JOIN user_infos ui ON rr.user_id = ui.user_id
        WHERE nc.notified_user_id = #{userId} AND nc.request_type = 'reimbursement'
       """)
    List<ReimJoinNotifyRecord> getNotifyReimbursementRecordByUserId(int userId);


    @Select("""
        SELECT rr.reimbursement_id,
               rr.user_id,
               rr.amount,
               rr.description,
               rr.status,
               rr.submitted_at,
               ui.username
        FROM reimbursement_requests rr
        JOIN user_infos ui ON rr.user_id = ui.user_id
        WHERE rr.review_user_id = #{user_id}
        """)
    List<ReimbursementRecord> getReviewReimbursementRecordByUserId(int user_id);

    @Update("UPDATE reimbursement_requests " +
            "SET description = #{description} , amount = #{amount}," +
            "status = #{status}, submitted_at=#{submitted_at}" +
            " WHERE user_id = #{user_id} and reimbursement_id = #{reimbursement_id}")
    int modifyReimbursementRecord(int user_id, int reimbursement_id, BigDecimal amount, String description,
                         String status, String submitted_at);


    @Insert("INSERT INTO reimbursement_requests (user_id, amount, description, status, submitted_at, review_user_id) " +
            "VALUES (#{user_id}, #{amount}, #{description}, #{status}, #{submitted_at}, #{review_user_id})")
    @Options(useGeneratedKeys = true, keyProperty = "reimbursement_id", keyColumn = "reimbursement_id")
    int addReimbursementRecord(int user_id, BigDecimal amount, String description,
                                String status, String submitted_at, int review_user_id);

    @Insert("INSERT INTO notification_chain (notified_user_id, request_type, request_id, notified_at, cc_user_id) " +
            "VALUES (#{notified_user_id}, #{request_type}, #{request_id}, #{notified_at}, #{cc_user_id})")
    @Options(useGeneratedKeys = true, keyProperty = "notification_id", keyColumn = "notification_id")
    int insertNotification(ReimJoinNotifyRecord record);


    @Delete("DELETE from reimbursement_requests WHERE reimbursement_id = #{reimbursement_id}")
    int deleteReimbursementRecord(int reimbursement_id);
}
