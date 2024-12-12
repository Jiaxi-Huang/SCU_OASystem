package com.example.backend.controllers;

import com.example.backend.entity.reimbursement.ReimbursementRecordWithAccessToken;
import com.example.backend.entity.ResponseBase;
import com.example.backend.entity.reimbursement.ReimbursementRecord;
import com.example.backend.services.AccessService;
import com.example.backend.services.ReimbursementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reimbursement")
public class ReimbursementCon {

    @Autowired
    private ReimbursementService reimbursementService;

    @Autowired
    private AccessService accessService;

    @PostMapping("/getReimbursementList")
    public ResponseBase getReimbursementRecord(@RequestBody ReimbursementRecordWithAccessToken request) {
        ResponseBase res = new ResponseBase();
        try {
            String accessToken = request.getAccessToken();
            System.out.println(accessToken);
            int userId = accessService.getAuthenticatedId(accessToken);
            request.setUser_id(userId);
            List<ReimbursementRecord> records = reimbursementService.getReimbursementRecordByUserId(userId);  // 获取所有记录
            res.setStatus(200);
            for (ReimbursementRecord record : records) {
                res.pushData(record);
            }
        } catch (Exception e) {
            res.setStatus(-1);
            res.setMessage(e.getMessage());
        }
        return res;
    }

    @PostMapping("/getAdminReimbursementList")
    public ResponseEntity<ResponseBase> getAdminReimbursementRecord() {
        ResponseBase res = new ResponseBase();
        try {
            List<ReimbursementRecord> records = reimbursementService.getAllRecords();  // 获取所有记录
            res.setStatus(200);
            for (ReimbursementRecord record : records) {
                res.pushData(record);
            }
        } catch (Exception e) {
            res.setStatus(-1);
            res.setMessage(e.getMessage());
            System.out.println("getAdminReimbursementList fail: " + e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }


    @PostMapping("/modifyReimbursementRecord")
    public ResponseBase modifyReimbursementRecord(@RequestBody ReimbursementRecord record) {
        int res_code = reimbursementService.modifyReimbursementRecord(record);
        return new ResponseBase();
    }

    @PostMapping("/addReimbursementRecord")
    public ResponseBase addReimbursementRecord(@RequestBody ReimbursementRecordWithAccessToken request) {
        ResponseBase res = new ResponseBase();
        int res_code = -1;
        try {
            String accessToken = request.getAccessToken();
            int userId = accessService.getAuthenticatedId(accessToken);
            request.setUser_id(userId);
            res_code = reimbursementService.addReimbursementRecord(request);
        } catch (Exception e) {
            res.setStatus(-1);
            res.setMessage(e.getMessage());
        }
        System.out.println("addRec res_code: " + res_code);
        return res;
    }

    @PostMapping("/deleteReimbursementRecord")
    public ResponseBase deleteReimbursementRecord(@RequestBody ReimbursementRecord record) {
        int res_code = reimbursementService.deleteReimbursementRecord(record);
        System.out.println("deleteRecord res_code: " + res_code);
        return new ResponseBase();
    }
}
