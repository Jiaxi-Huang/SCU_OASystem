package com.example.backend.controllers;

import com.example.backend.entity.leave.LeaveApprovalRecord;
import com.example.backend.entity.ResponseBase;
import com.example.backend.entity.leave.LeaveApprovalRecordWithAccessToken;
import com.example.backend.entity.leave.LeaveJoinNotifyRecord;
import com.example.backend.entity.leave.LeaveJoinNotifyRecordWithAccessToken;
import com.example.backend.entity.reimbursement.ReimbursementRecord;
import com.example.backend.entity.reimbursement.ReimbursementRecordWithAccessToken;
import com.example.backend.services.AccessService;
import com.example.backend.services.LeaveApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/leaveApproval")
public class LeaveApprovalCon {

    @Autowired
    private LeaveApprovalService leave_service;

    @Autowired
    private AccessService accessService;

    @PostMapping("/getMyLeaveRecord")
    public ResponseBase getMyLeaveRecord(@RequestBody LeaveApprovalRecordWithAccessToken request) {
        ResponseBase res = new ResponseBase();
        try {
            String accessToken = request.getAccessToken();
            int userId = accessService.getAuthenticatedId(accessToken);
            request.setUser_id(userId);
            List<LeaveApprovalRecord> records = leave_service.getMyLeaveRecord(userId);
            res.setStatus(0);
            for (LeaveApprovalRecord record : records) {
                res.pushData(record);
            }
        } catch (Exception e) {
            res.setStatus(-1);
            res.setMessage(e.getMessage());
        }
        return res;
    }
    @PostMapping("/getNotifyLeaveRecord")
    public ResponseBase getNotifyLeaveRecord(@RequestBody LeaveJoinNotifyRecordWithAccessToken request) {
        ResponseBase res = new ResponseBase();
//        try {
            String accessToken = request.getAccessToken();
            int userId = accessService.getAuthenticatedId(accessToken);
            request.setUser_id(userId);
            List<LeaveJoinNotifyRecord> records = leave_service.getNotifyLeaveRecord(userId);
            res.setStatus(0);
            for (LeaveJoinNotifyRecord record : records) {
                res.pushData(record);
            }
//        } catch (Exception e) {
//            res.setStatus(-1);
//            res.setMessage(e.getMessage());
//        }
        return res;
    }
    @PostMapping("/getReviewLeaveRecord")
    public ResponseBase getReviewLeaveRecord(@RequestBody LeaveApprovalRecordWithAccessToken request) {
        ResponseBase res = new ResponseBase();
        try {
            String accessToken = request.getAccessToken();
            int userId = accessService.getAuthenticatedId(accessToken);
            request.setUser_id(userId);
            List<LeaveApprovalRecord> records = leave_service.getReviewLeaveRecord(userId);
            res.setStatus(0);
            for (LeaveApprovalRecord record : records) {
                res.pushData(record);
            }
        } catch (Exception e) {
            res.setStatus(-1);
            res.setMessage(e.getMessage());
        }
        return res;
    }
    @PostMapping("/getAdminLeaveRecord")
    public ResponseBase getAdminLeaveRecord() {
        ResponseBase res = new ResponseBase();
        try {
            List<LeaveApprovalRecord> records = leave_service.getAllRecords();  // 获取所有记录
            System.out.println("getAdminLeaveRecord: " + res);
            res.setStatus(0);
            for (LeaveApprovalRecord record : records) {
                res.pushData(record);
            }
        } catch (Exception e) {
            res.setStatus(-1);
            res.setMessage(e.getMessage());
        }
        return res;
    }

    @PostMapping("/modifyLeaveRecord")
    public ResponseEntity<ResponseBase> modifyLeaveRecord(@RequestBody LeaveApprovalRecord record) {
        ResponseBase response = new ResponseBase();
        int res_code = leave_service.updateLeaveApprovalRecord(record);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/addLeaveRecord")
    public ResponseBase addLeaveRecord(@RequestBody LeaveApprovalRecordWithAccessToken request) {
        System.out.println("addLeaveRecord: " + (request).toString());
        ResponseBase res = new ResponseBase();
        try {
            String accessToken = request.getAccessToken();
            int userId = accessService.getAuthenticatedId(accessToken);
            request.setUser_id(userId);

            // 插入请假记录
            int leave_id = leave_service.insertLeaveApprovalRecord(request);
            res.setStatus(200);
            res.setMessage("Leave record added successfully.");
            res.pushData(leave_id);

            // 处理抄送人
            if (request.getCc_user() != null) {
                for (int ccUserId : request.getCc_user()) {
                    LeaveJoinNotifyRecord ccRecord = new LeaveJoinNotifyRecord(
                            0, userId, request.getStart_date(), request.getEnd_date(),
                            request.getType(), request.getReason(), request.getStatus(),
                            request.getSubmitted_at(), 0, userId, ccUserId, leave_id, "leave", request.getSubmitted_at()
                    );
                    leave_service.addNotification(ccRecord);
                }
            }

        } catch (Exception e) {
            res.setStatus(-1);
            res.setMessage(e.getMessage());
        }

        return res;
    }


    @PostMapping("/deleteLeaveRecord")
    public ResponseEntity<ResponseBase> deleteLeaveRecord(@RequestBody LeaveApprovalRecord record) {
        ResponseBase response = new ResponseBase();
        int res_code = leave_service.deleteRecord(record);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
