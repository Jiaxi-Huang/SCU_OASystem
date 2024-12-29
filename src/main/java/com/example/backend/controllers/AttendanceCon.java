package com.example.backend.controllers;


import com.example.backend.entity.*;
import com.example.backend.mapper.UserMapper;
import com.example.backend.services.AttendanceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/api/attendance")
public class AttendanceCon {
    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    UserMapper userMapper;

    public String setStatus(Attendance record){
        // 定义上班和下班的标准时间
        LocalTime workStartTime = LocalTime.of(9, 0); // 上班时间 9:00
        LocalTime workEndTime = LocalTime.of(18, 0);  // 下班时间 18:00

        // 判断迟到或早退状态
        if (record.getCheckIn() != null&&record.getCheckOut() != null) {
            if (record.getCheckIn().isAfter(workStartTime)&&record.getCheckOut().isAfter(workEndTime)) {
                return "Late"; // 迟到
            }
            if (record.getCheckOut().isBefore(workEndTime)&&record.getCheckIn().isBefore(workStartTime)) {
                return "Leave Early"; // 早退
            }
            if (record.getCheckOut().isAfter(workEndTime)&&record.getCheckIn().isBefore(workStartTime)) {
                return "On Time"; // 早退
            }
            if (record.getCheckOut().isBefore(workEndTime)&&record.getCheckIn().isAfter(workStartTime)) {
                return "Late And Leave Early"; // 早退
            }
        }
        // 如果没有考勤记录，设为缺席
        if (record.getCheckIn() == null || record.getCheckOut() == null) {
            return "Absent"; // 缺席
        }
        return null;
    }

    @PostMapping("/loadAttendance")
    public ResponseBase loadAttendance() {
        ResponseBase res = new ResponseBase();
        System.out.println("进入后端Attendance");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date=sdf.format(calendar.getTime());
        System.out.println(date);
        try {
            System.out.println("1111");
            List<Attendance> records=attendanceService.getAttendanceRecord(date);


            System.out.println("1111");
            for (Attendance record : records) {
                User userInfo = userMapper.findByUserId(record.getUserId());
                record.setUserName(userInfo.getUsername());
                record.setDepartment(userInfo.getDepartment());
                record.setRole(userInfo.getRole());
                res.pushData(record);
            }
            System.out.println("1111");
            String ip = "220.248.12.158"; // IpRegion:上海
//        String ip = "47.52.236.180"; // IpRegion:香港
//        String ip = "172.22.12.123"; // IpRegion:内网IP
//        String ip = "164.114.53.60"; // IpRegion:美国
            String ipRegion = IpUtil.getIpRegion(ip);
            System.out.println(ipRegion);

//            InetAddress addr = InetAddress.getLocalHost();
//            System.out.println("Local HostAddress: "+addr.getHostAddress());
//            String ipRegion = IpUtil.getIpRegion(addr.getHostAddress());
//            System.out.println(ipRegion);
            res.setStatus(0);
        }
        catch (Exception e) {
            res.setStatus(-1);
            System.out.println(e);
            res.setMessage(e.getMessage());
        }
        return res;
    }

    @PostMapping("/addAttendance")
    public ResponseEntity<ResponseBase> addAttendance(@RequestBody Attendance record) {
        ResponseBase res = new ResponseBase();

        User userInfo = null;
        List<User> userList=userMapper.findAllUser();
        for (User user : userList) {
            if(Objects.equals(user.getUsername(), record.getUserName())){
                userInfo=user;
                break;
            }
        }
        record.setUserId(userInfo.getUserId());
        record.setStatus(setStatus(record));

        // 调用AttendanceService添加考勤记录
        int res_code = attendanceService.addAttendance(record);


        if (res_code==0) {
            res.setStatus(-1);
        }
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }



    @PostMapping("/delAttendance")
    public ResponseEntity<ResponseBase> delAttendance(@RequestParam int id) {
        ResponseBase res = new ResponseBase();
        // 调用AttendanceService添加考勤记录
        int res_code = attendanceService.delAttendance(id);


        if (res_code==0) {
            res.setStatus(-1);
        }
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @PostMapping("/editAttendance")
    public ResponseEntity<ResponseBase> editAttendance(@RequestBody Attendance record) {
        ResponseBase res = new ResponseBase();
        // 调用AttendanceService添加考勤记录

        record.setStatus(setStatus(record));
        int res_code = attendanceService.editAttendance(record);


        if (res_code==0) {
            res.setStatus(-1);
        }
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }



}
