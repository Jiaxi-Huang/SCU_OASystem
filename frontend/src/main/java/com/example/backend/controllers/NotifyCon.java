package com.example.backend.controllers;

import com.example.backend.entity.NotifyRecord;
import com.example.backend.entity.ResponseBase;
import com.example.backend.entity.User;
import com.example.backend.entity.reimbursement.ReimbursementRecordWithAccessToken;
import com.example.backend.services.AccessService;
import com.example.backend.services.NotifyService;
import com.example.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/notification")
public class NotifyCon {
    @Autowired
    private NotifyService notifyService;

    @Autowired
    private UserService userService;

    @Autowired
    private AccessService accessService;

    @PostMapping("/addNotification")
    public ResponseBase addNotification(@RequestBody NotifyRecord request) {
        ResponseBase res = new ResponseBase();
        try {
            String accessToken = request.getAccessToken();
            int userId = accessService.getAuthenticatedId(accessToken);
            request.setUser_id(userId);
            notifyService.addNotification(request);
            res.setStatus(200);
            res.setMessage("Notification record added successfully.");
        } catch (Exception e) {
            res.setStatus(-1);
            res.setMessage(e.getMessage());
        }
        return res;
    }

    @PostMapping("/getAllUsers")
    public ResponseBase getAllUsers() {
//        System.out.println("[getAllUsers] begin============================");
        ResponseBase res = new ResponseBase();
        try {
            res.pushData(userService.adminUserInfo(1));
            res.setStatus(200);
            res.setMessage("Get all users successfully.");
        } catch (Exception e) {
            res.setStatus(-1);
            res.setMessage(e.getMessage());
        }
        return res;
    }
}
