package com.example.backend.services;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.entity.leave.LeaveApprovalRecord;
import com.example.backend.entity.leave.LeaveApprovalRecordWithAccessToken;
import com.example.backend.entity.leave.LeaveJoinNotifyRecord;
import com.example.backend.mapper.LeaveApprovalMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveApprovalService extends ServiceImpl<LeaveApprovalMapper, LeaveApprovalRecord> {
    @Autowired
    private LeaveApprovalMapper leaveApprovalMapper;

    public List<LeaveApprovalRecord> getAllRecords() {
        return  leaveApprovalMapper.getAll();
    }

    public List<LeaveApprovalRecord> getMyLeaveRecord(int userId) {
        return leaveApprovalMapper.getMyLeaveRecord(userId);
    }
    public List<LeaveJoinNotifyRecord> getNotifyLeaveRecord(int userId) {
        return leaveApprovalMapper.getNotifyLeaveRecord(userId);
    }
    public List<LeaveApprovalRecord> getReviewLeaveRecord(int userId) {
        return leaveApprovalMapper.getReviewLeaveRecord(userId);
    }
    public int updateLeaveApprovalRecord(LeaveApprovalRecord record) {
        int res_code = leaveApprovalMapper.updateLeaveApprovalRecord(
                record.getLeave_id(), record.getUser_id(), record.getStart_date(),
                record.getEnd_date(), record.getReason(), record.getStatus(), record.getSubmitted_at());
        return res_code;
    }

    public int insertLeaveApprovalRecord(LeaveApprovalRecordWithAccessToken record) {
        return leaveApprovalMapper.insertLeaveApprovalRecord(
                record.getLeave_id(), record.getUser_id(), record.getReview_user_id(), record.getStart_date(),
                record.getEnd_date(), record.getType(),record.getReason(), record.getStatus(), record.getSubmitted_at());
    }

    public void addNotification(LeaveJoinNotifyRecord record) {
        leaveApprovalMapper.insertNotification(record);
    }


    public int deleteRecord(LeaveApprovalRecord record) {
        int res_code = leaveApprovalMapper.deleteRecord(record.getLeave_id());
        return res_code;
    }
}
