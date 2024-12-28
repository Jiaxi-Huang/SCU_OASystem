package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.Attendance;
import com.example.backend.entity.Files;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Mapper
public interface AttendanceMapper extends BaseMapper<Attendance> {
    @Select(" SELECT * FROM attendance WHERE DATE(attendance_date) = #{date}")
    List<Attendance> getAttendanceRecord(String date);

    @Insert("INSERT INTO attendance ( user_id, attendance_date, check_in, check_out, status, location )" +
            " VALUES( #{userId}, #{attendanceDate}, #{checkIn}, #{checkOut}, #{status}, #{location} )")
    int addAttendance(Integer userId, LocalDate attendanceDate,
                      LocalTime checkIn, LocalTime checkOut, String status, String location);

    @Delete("DELETE FROM attendance WHERE id = #{id}")
    int delAttendance(int id);

    @Update(" UPDATE attendance SET check_in = #{checkIn}, check_out = #{checkOut}, status = #{status} WHERE id = #{id}")
    int editAttendance(Integer id, LocalTime checkIn, LocalTime checkOut, String status);



}
