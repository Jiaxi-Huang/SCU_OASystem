package com.example.backend.controllers;

import com.example.backend.entity.LeaveApprovalRecord;
import com.example.backend.entity.ResponseBase;
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

    @PostMapping("/getLeaveRecord")
    public ResponseEntity<ResponseBase> getLeaveRecord() {
        ResponseBase response = new ResponseBase();
        List<LeaveApprovalRecord> records = leave_service.getAllRecords();

        for (LeaveApprovalRecord record : records) {
            response.pushData(record);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/modifyLeaveRecord")
    public ResponseEntity<ResponseBase> modifyLeaveRecord(@RequestBody LeaveApprovalRecord record) {
        ResponseBase response = new ResponseBase();
        int res_code = leave_service.updateLeaveApprovalRecord(record);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/addLeaveRecord")
    public ResponseEntity<ResponseBase> addLeaveRecord(@RequestBody LeaveApprovalRecord record) {
        ResponseBase response = new ResponseBase();
        int res_code = leave_service.insertLeaveApprovalRecord(record);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PostMapping("/deleteLeaveRecord")
    public ResponseEntity<ResponseBase> deleteLeaveRecord(@RequestBody LeaveApprovalRecord record) {
        ResponseBase response = new ResponseBase();
        int res_code = leave_service.deleteRecord(record);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
