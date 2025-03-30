package com.example.backend.services;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.entity.reimbursement.ReimJoinNotifyRecord;
import com.example.backend.entity.reimbursement.ReimbursementRecord;
import com.example.backend.entity.reimbursement.ReimbursementRecordWithAccessToken;
import com.example.backend.mapper.ReimbursementMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReimbursementService extends ServiceImpl<ReimbursementMapper, ReimbursementRecord> {

    @Autowired
    private ReimbursementMapper ReimbursementMapper;

    public List<ReimbursementRecord> getAllRecords() {
        return  ReimbursementMapper.getAll();
    }

    public List<ReimbursementRecord> getReimbursementRecordByUserId(int user_id) {
        return ReimbursementMapper.getReimbursementRecordByUserId(user_id);
    }

    public List<ReimbursementRecord> getReviewReimbursementRecordByUserId(int review_user_id) {
        return ReimbursementMapper.getReviewReimbursementRecordByUserId(review_user_id);
    }

    public List<ReimJoinNotifyRecord> getNotifyReimbursementRecordByUserId(int notified_user_id) {
        return ReimbursementMapper.getNotifyReimbursementRecordByUserId(notified_user_id);
    }


    public int modifyReimbursementRecord(ReimbursementRecord record) {
        int res_code = ReimbursementMapper.modifyReimbursementRecord(
                record.getReimbursement_id(), record.getUser_id(), record.getAmount(), record.getDescription(),
                record.getStatus(), record.getSubmitted_at());
        return res_code;
    }

    public int addReimbursementRecord(ReimbursementRecordWithAccessToken record) {
        return ReimbursementMapper.addReimbursementRecord(
                record.getUser_id(), record.getAmount(), record.getDescription(),
                record.getStatus(), record.getSubmitted_at(), record.getReview_user_id());
    }

    public void addNotification(ReimJoinNotifyRecord record) {
        // 这里假设你有一个 NotificationMapper 来处理通知的插入
        ReimbursementMapper.insertNotification(record);
    }


    public int deleteReimbursementRecord(ReimbursementRecord record) {
        int res_code = ReimbursementMapper.deleteReimbursementRecord(record.getReimbursement_id());
        return res_code;
    }

}
