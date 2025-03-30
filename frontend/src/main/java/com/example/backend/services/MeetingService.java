package com.example.backend.services;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.entity.ResponseBase;
import com.example.backend.entity.meeting.MeeetingWithTk;
import com.example.backend.entity.meeting.Meeting;
import com.example.backend.entity.meeting.MeetingWithAdderId;
import com.example.backend.entity.meeting.MeetingWithMultiUsers;
import com.example.backend.mapper.MeetingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

@Service
public class MeetingService extends ServiceImpl<MeetingMapper, MeetingWithAdderId> {

    @Autowired
    private MeetingMapper meetingMapper;

    public ResponseBase getPersonalMeetings(int user_id) {
        ResponseBase response = new ResponseBase();
        List<MeetingWithAdderId> meetings = meetingMapper.getPersonalMeetings(user_id);
        List<MeetingWithAdderId> scheduled = new ArrayList<>();
        List<MeetingWithAdderId> processing = new ArrayList<>();
        List<MeetingWithAdderId> passed  = new ArrayList<>();

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (MeetingWithAdderId meeting : meetings) {
            try {
                LocalDate mt_dt = LocalDateTime.parse(meeting.getMtin_st().trim(), formatter).toLocalDate();
//                System.out.println("Parsed date: " + mt_dt);
                if (mt_dt.isAfter(today)) {
                    scheduled.add(meeting);
                } else if (mt_dt.isBefore(today)) {
                    passed.add(meeting);
                } else {
                    processing.add(meeting);
                }

            } catch (Exception e) {
                System.err.println("Failed to parse date for meeting: " + meeting.getMtin_st());
                response.setStatus(-1);
                e.printStackTrace();
            }
        }
        response.pushData(scheduled);
        response.pushData(processing);
        response.pushData(passed);

        return response;
    }

    public ResponseBase searchByAnything(String field, String key, int user_id) {
        ResponseBase response = new ResponseBase();
        List<MeetingWithAdderId> meetings = meetingMapper.searchByFieldAndKeyPersonal(field, key, user_id);;
        List<MeetingWithAdderId> scheduled = new ArrayList<>();
        List<MeetingWithAdderId> processing = new ArrayList<>();
        List<MeetingWithAdderId> passed  = new ArrayList<>();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        int cnt = 0;
        for (MeetingWithAdderId meeting : meetings) {
            ++cnt;
            try {
                LocalDate mt_dt = LocalDateTime.parse(meeting.getMtin_st().trim(), formatter).toLocalDate();
                if (mt_dt.isAfter(today)) {
                    scheduled.add(meeting);
                } else if (mt_dt.isBefore(today)) {
                    passed.add(meeting);
                } else {
                    processing.add(meeting);
                }

            } catch (Exception e) {
                System.err.println("Failed to parse date for meeting: " + meeting.getMtin_st());
                response.setStatus(-1);
                e.printStackTrace();
            }
        }
        response.pushData(scheduled);
        response.pushData(processing);
        response.pushData(passed);
        response.pushData(cnt);
        return response;
    }

    public ResponseBase updateMeeting(int user_id, MeeetingWithTk meeting) {
        ResponseBase response = new ResponseBase();
//        System.out.println(meeting.getMtin_title());
        try {
            if (meeting.getMtin_id() >= 0 && user_id >= 0) {
                meetingMapper.updateMeetingAdder(user_id, meeting.getAdder_id(), meeting.getMtin_id());
            }

            if (meeting.getMtin_id() >= 0) {
                meetingMapper.updateMeeting(meeting.getMtin_id(), meeting.getMtin_title(), meeting.getMtin_ctnt(), meeting.getMtin_fin(),
                        meeting.getMtin_st(), meeting.getMtin_len(), meeting.getMtin_crt(), meeting.getMtin_host(), meeting.getMtin_loc());
            }
        }catch (Exception e) {
            response.setStatus(-1);
            e.printStackTrace();
        }
        return response;
    }

    public void createMeeting(Meeting meeting) {
        meetingMapper.createMeeting(meeting);
    }

    public void addMeetingToUserId(int user_id, int meeting_id, int adder_id) {
        meetingMapper.addMeetingForSomeone(user_id, meeting_id, adder_id);
    }

    public void deleteMeetingPersonally(int user_id, int mtin_id) {
        meetingMapper.deleteUserMeetings(user_id, mtin_id);
    }


    public void addMeetingPersonally(int user_id, int mtin_id) {
        meetingMapper.addMeetingPersonally(user_id, mtin_id);
    }

    public Meeting searchByMtinId(int mtin_id) {
        return meetingMapper.searchByMtinId(mtin_id);
    }
}
