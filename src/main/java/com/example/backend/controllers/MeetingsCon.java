package com.example.backend.controllers;

import com.example.backend.entity.ResponseBase;
import com.example.backend.entity.meeting.Meeting;
import com.example.backend.entity.meeting.MeetingWithAdderId;
import com.example.backend.entity.userInfo.adminUserInfoRequest;
import com.example.backend.services.AccessService;
import com.example.backend.services.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/meetings")
public class MeetingsCon {

    @Autowired
    private AccessService accessService;

    @Autowired
    private MeetingService meetingService;

    @PostMapping("/getMyMeetings")
    public ResponseBase getRec(@RequestBody adminUserInfoRequest request) {
        ResponseBase res = new ResponseBase();

        try {
            String accessToken = request.getAccessToken();
            int userId = accessService.getAuthenticatedId(accessToken);
//            System.out.println(userId);
            res = meetingService.getPersonalMeetings(userId);
            if (res.getStatus() != 0) {
                throw new Exception();
            }
        }
        catch (Exception e) {
            res.setStatus(-1);
            res.setMessage(e.getMessage());
        }
        return res;
    }

}
