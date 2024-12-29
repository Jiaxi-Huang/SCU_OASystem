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

    @Insert("INSERT INTO attendance ( user_id, attendance_date, check_in, check_out, status, inLocation, outLocation )" +
            " VALUES( #{userId}, #{attendanceDate}, #{checkIn}, #{checkOut}, #{status}, #{inLocation}, #{outLocation} )")
    int addAttendance(Integer userId, LocalDate attendanceDate,
                      LocalTime checkIn, LocalTime checkOut, String status, String inLocation,String outLocation);

    @Delete("DELETE FROM attendance WHERE id = #{id}")
    int delAttendance(int id);

    @Update(" UPDATE attendance SET attendance_date = #{attendanceDate},check_in = #{checkIn}, check_out = #{checkOut}, inLocation=#{inLocation},outLocation=#{outLocation}, status = #{status} WHERE id = #{id}")
    int editAttendance(Integer id,LocalDate attendanceDate, LocalTime checkIn, LocalTime checkOut, String inLocation,String outLocation,String status);

    @Select(" SELECT * FROM attendance WHERE user_id = #{userId}")
    List<Attendance> getPersonalAttendanceRecord(int userId);

    @Select("<script>" +
            "SELECT * FROM attendance WHERE id IN " +
            "<foreach item='id' collection='ids' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    List<Attendance> excelAttendance(@Param("ids") List<Integer> ids);

}
