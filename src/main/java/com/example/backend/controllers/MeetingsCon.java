package com.example.backend.controllers;

import com.example.backend.entity.ResponseBase;
import com.example.backend.entity.meeting.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @PostMapping("/createMeeting")
    public ResponseBase createMeeting(@RequestBody MeetingWithMultiUsers meetingMultiUsers) {
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

            meetingService.addMeetingToUserId(userId, mtin_id, userId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            res.setStatus(-1);
            res.setMessage(e.getMessage());
        }
        return res;
    }

    @PostMapping("/deleteMeetingPersonally")
    public ResponseBase deleteMeetingPersonally(@RequestBody MeetingIdWithToken record) {
        ResponseBase res = new ResponseBase();

        try {
            int user_id = accessService.getAuthenticatedId(record.getAccessToken());
            int mtin_id = record.getMtin_id();
            meetingService.deleteMeetingPersonally(user_id, mtin_id);
        } catch (Exception e) {
            res.setStatus(-1);
            res.setMessage(e.getMessage());
        }
        return res;
    }

    @PostMapping("/addMeetingPersonally")
    public ResponseBase addMeetingPersonally(@RequestBody MeetingIdWithToken record) {
        ResponseBase res = new ResponseBase();
        try {
            int user_id = accessService.getAuthenticatedId(record.getAccessToken());
            int mtin_id = record.getMtin_id();
            meetingService.addMeetingPersonally(user_id, mtin_id);
        } catch (Exception e) {
            res.setStatus(-1);
            res.setMessage(e.getMessage());
        }
        return res;
    }

    @PostMapping("/search_by_mtin_id")
    public ResponseBase searchByMtinId(@RequestBody ResponseBase record) {
        ResponseBase res = new ResponseBase();

        try {
            int mtin_id = (Integer) record.getData().get(0);
            Meeting meeting = meetingService.searchByMtinId(mtin_id);

            if (meeting == null ) {
                System.out.println("meeting is null");
                res.setStatus(1);
            } else {
                res.pushData(meeting);
            }
        }
        catch (Exception e) {
            res.setStatus(-1);
            res.setMessage(e.getMessage());
        }
        return res;
    }
}
