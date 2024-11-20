package com.example.backend.services;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.entity.LeaveApprovalRecord;
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

    public int updateLeaveApprovalRecord(LeaveApprovalRecord record) {
        int res_code = leaveApprovalMapper.updateLeaveApprovalRecord(
                record.getLeave_id(), record.getUser_id(), record.getStart_date(),
                record.getEnd_date(), record.getReason(), record.getStatus(), record.getSubmitted_at());
        return res_code;
    }

    public int insertLeaveApprovalRecord(LeaveApprovalRecord record) {
        int res_code = leaveApprovalMapper.insertLeaveApprovalRecord(
                record.getLeave_id(),record.getUser_id(),record.getStart_date(), 
                record.getEnd_date(), record.getReason(),
                record.getStatus(), record.getSubmitted_at());
        return res_code;
    }


    public int deleteRecord(LeaveApprovalRecord record) {
        int res_code = leaveApprovalMapper.deleteRecord(record.getLeave_id());
        return res_code;
    }
}
