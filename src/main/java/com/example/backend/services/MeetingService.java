package com.example.backend.services;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.entity.ResponseBase;
import com.example.backend.entity.meeting.Meeting;
import com.example.backend.entity.meeting.MeetingWithAdderId;
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

}
