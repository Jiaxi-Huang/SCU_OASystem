package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.reimbursement.ReimbursementRecord;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;


@Mapper
public interface ReimbursementMapper extends BaseMapper<ReimbursementRecord> {

    @Select("SELECT * FROM reimbursement_requests")
    List<ReimbursementRecord> getAll();

    @Update("UPDATE reimbursement_requests " +
            "SET description = #{description} , amount = #{amount}," +
            "status = #{status}, submitted_at=#{submitted_at}" +
            " WHERE user_id = #{user_id} and reimbursement_id = #{reimbursement_id}")
    int modifyReimbursementRecord(int user_id, int reimbursement_id, BigDecimal amount, String description,
                         String status, String submitted_at);


    @Insert("INSERT INTO reimbursement_requests (description, amount, status,submitted_at, user_id)" +
            " VALUES(#{description}, #{amount}, #{status}, #{submitted_at}, #{user_id})")
    int addReimbursementRecord(int user_id, int reimbursement_id, String description,
                         BigDecimal amount, String status, String submitted_at);

    @Delete("DELETE from reimbursement_requests WHERE reimbursement_id = #{reimbursement_id}")
    int deleteReimbursementRecord(int reimbursement_id);

    @Select("SELECT * FROM reimbursement_requests WHERE user_id = #{user_id}")
    List<ReimbursementRecord> getReimbursementRecordByUserId(int user_id);
}
