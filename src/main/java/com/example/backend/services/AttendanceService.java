package com.example.backend.services;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.entity.Attendance;

import com.example.backend.mapper.AttendanceMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AttendanceService extends ServiceImpl<AttendanceMapper, Attendance> {

    @Autowired
    private  AttendanceMapper attendanceMapper;

    public int addAttendance(Attendance record) {
        return attendanceMapper.addAttendance(record.getUserId(), record.getAttendanceDate(),
                record.getCheckIn(), record.getCheckOut(), record.getStatus(), record.getLocation());
    }

    public List<Attendance> getAttendanceRecord(String date) {
        return attendanceMapper.getAttendanceRecord(date);

    }

    public int delAttendance(int id) {
        return attendanceMapper.delAttendance(id);
    }

    public int editAttendance(Attendance record) {
        return attendanceMapper.editAttendance(record.getId(),record.getCheckIn(),record.getCheckOut(),record.getStatus());
    }
}
