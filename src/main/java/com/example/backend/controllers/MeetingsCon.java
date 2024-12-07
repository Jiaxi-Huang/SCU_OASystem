package com.example.backend.controllers;

import com.example.backend.entity.ResponseBase;
import com.example.backend.entity.meeting.MeeetingWithTk;
import com.example.backend.entity.meeting.Meeting;
import com.example.backend.entity.meeting.MeetingWithAdderId;
import com.example.backend.entity.meeting.MeetingWithMultiUsers;
import com.example.backend.entity.userInfo.adminUserInfoRequest;
import com.example.backend.services.AccessService;
import com.example.backend.services.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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


    @PostMapping("/updateMeeting")
    public ResponseBase updateMeeting(@RequestBody MeeetingWithTk meetingWithTk) {
        ResponseBase res = new ResponseBase();

        try {
            String accessToken = meetingWithTk.getAcsTkn();
            int userId = accessService.getAuthenticatedId(accessToken);
            meetingService.updateMeeting(userId, meetingWithTk);
        } catch (Exception e) {
            res.setStatus(-1);
            res.setMessage(e.getMessage());
        }
        return res;
    }

    @PostMapping("/distributed_create")
    public ResponseBase distributedCreateMeeting(@RequestBody MeetingWithMultiUsers meetingMultiUsers) {
        ResponseBase res = new ResponseBase();
        try {
            String accessToken = meetingMultiUsers.getAccessToken();
            int userId = accessService.getAuthenticatedId(accessToken);

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String time = now.format(formatter);
            Meeting this_meeting = new Meeting(
                    -1,
                    meetingMultiUsers.getMtin_title(),
                    meetingMultiUsers.getMtin_ctnt(),
                    meetingMultiUsers.getMtin_st(),
                    meetingMultiUsers.getMtin_fin(),
                    meetingMultiUsers.getMtin_len(),
                    Integer.toString(userId),
                    meetingMultiUsers.getMtin_loc(),
                    time);
            meetingService.createMeeting(this_meeting);
            int mtin_id = this_meeting.getMtin_id();

            for (int id : meetingMultiUsers.getUser_ids()) {
                meetingService.addMeetingToUserId(id, mtin_id, userId);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            res.setStatus(-1);
            res.setMessage(e.getMessage());
        }
        return res;
    }
}
