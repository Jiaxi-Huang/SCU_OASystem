package com.example.backend.controllers;

import com.example.backend.entity.User;
import com.example.backend.entity.reimbursement.ReimJoinNotifyRecordWithAccessToken;
import com.example.backend.entity.reimbursement.ReimJoinNotifyRecord;
import com.example.backend.entity.reimbursement.ReimbursementRecordWithAccessToken;
import com.example.backend.entity.ResponseBase;
import com.example.backend.entity.reimbursement.ReimbursementRecord;
import com.example.backend.entity.todoList.TodoRecord;
import com.example.backend.entity.userInfo.adminUserInfoRequest;
import com.example.backend.services.AccessService;
import com.example.backend.services.ReimbursementService;
import com.example.backend.services.UserService;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.util.List;

@RestController
@RequestMapping("/api/reimbursement")
public class ReimbursementCon {

    @Autowired
    private ReimbursementService reimbursementService;

    @Autowired
    private AccessService accessService;

    @Autowired
    private UserService userService;

    @PostMapping("/getReimbursementList")
    public ResponseBase getReimbursementRecord(@RequestBody ReimbursementRecordWithAccessToken request) {
        ResponseBase res = new ResponseBase();
        try {
            String accessToken = request.getAccessToken();
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

    @PostMapping("/getReviewReimbursementList")
    public ResponseBase getReviewReimbursementRecord(@RequestBody ReimbursementRecordWithAccessToken request) {
        ResponseBase res = new ResponseBase();
        try {
            String accessToken = request.getAccessToken();
            int userId = accessService.getAuthenticatedId(accessToken);
            request.setUser_id(userId);
            List<ReimbursementRecord> records = reimbursementService.getReviewReimbursementRecordByUserId(userId);
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

    @PostMapping("/getNotifyReimbursementList")
    public ResponseBase getNotifyReimbursementRecord(@RequestBody ReimJoinNotifyRecordWithAccessToken request) {
        ResponseBase res = new ResponseBase();
        try {
            String accessToken = request.getAccessToken();
            int userId = accessService.getAuthenticatedId(accessToken);
            request.setUser_id(userId);
//            System.out.println("getNotifyReimbursementList user_id: "+userId);
            List<ReimJoinNotifyRecord> records = reimbursementService.getNotifyReimbursementRecordByUserId(userId);
            res.setStatus(200);
            for (ReimJoinNotifyRecord record : records) {
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
            List<ReimbursementRecord> records = reimbursementService.getAllRecords();
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
        try {
            int reimbursement_id;
            String accessToken = request.getAccessToken();
            int userId = accessService.getAuthenticatedId(accessToken);
            request.setUser_id(userId);
            reimbursement_id = reimbursementService.addReimbursementRecord(request);
            res.setStatus(200);
            res.setMessage("Reimbursement record added successfully.");
            res.pushData(reimbursement_id);

            User user = userService.userInfo(userId);
            // 处理抄送人
            if (request.getCc_user() != null) {
                for (int ccUserId : request.getCc_user()) {
                    ReimJoinNotifyRecord ccRecord = new ReimJoinNotifyRecord(
                            0, userId, request.getAmount(), request.getDescription(),
                            request.getStatus(), request.getSubmitted_at(), 0, ccUserId,
                            "reimbursement", reimbursement_id, request.getSubmitted_at(), 0, user.getUsername()
                    );
                    reimbursementService.addNotification(ccRecord);
                }
            }

        } catch (Exception e) {
            res.setStatus(-1);
            res.setMessage(e.getMessage());
        }

        return res;
    }

    @PostMapping("/deleteReimbursementRecord")
    public ResponseBase deleteReimbursementRecord(@RequestBody ReimbursementRecord record) {
        int res_code = reimbursementService.deleteReimbursementRecord(record);
        System.out.println("deleteRecord res_code: " + res_code);
        return new ResponseBase();
    }


}
