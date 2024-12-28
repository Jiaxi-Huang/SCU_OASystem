package com.example.backend.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
public class Attendance {

    private Integer id;  // 考勤记录ID
    private Integer userId;  // 员工ID（外键关联员工表）
    private LocalDate attendanceDate;  // 考勤日期
    private LocalTime checkIn;  // 上班打卡时间
    private LocalTime checkOut;  // 下班打卡时间
    private String status;  // 考勤状态（准时、迟到、早退、缺勤）
    private String userName;
    private String department;
    private String role;
    private String location;
}
