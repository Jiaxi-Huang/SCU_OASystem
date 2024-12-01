package com.example.backend.controllers;

import com.example.backend.entity.ResponseBase;
import com.example.backend.entity.ReimbursementRecord;
import com.example.backend.entity.userInfo.adminUserInfoRequest;
import com.example.backend.services.AccessService;
import com.example.backend.services.ReimbursementService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/getReimbursementRecord")
    public ResponseBase getReimbursementRecord(@RequestBody adminUserInfoRequest request) {
        ResponseBase res = new ResponseBase();

        try {
            String accessToken = request.getAccessToken();
            int userId = accessService.getAuthenticatedId(accessToken);
            List<ReimbursementRecord> records = reimbursementService.getReimbursementRecordByUserId(userId);

            for (ReimbursementRecord record : records) {
                res.pushData(record);
            }
        }

        catch (Exception e) {
            res.setStatus(-1);
            res.setMessage(e.getMessage());
        }
        return res;
    }

    @PostMapping("/modifyReimbursementRecord")
    public ResponseBase modifyReimbursementRecord(@RequestBody ReimbursementRecord record) {
        int res_code = reimbursementService.modifyReimbursementRecord(record);
//        System.out.println("modifyRec res_code: " + res_code);
        return new ResponseBase();
    }

    @PostMapping("/addReimbursementRecord")
    public ResponseBase addReimbursementRecord(@RequestBody ReimbursementRecord request) {
        ResponseBase res = new ResponseBase();
        int res_code = -1;
        try {
            String accessToken = request.getAcsTkn();
            System.out.println(accessToken);
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
