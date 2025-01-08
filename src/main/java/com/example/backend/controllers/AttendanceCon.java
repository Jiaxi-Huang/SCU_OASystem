package com.example.backend.controllers;


import com.example.backend.entity.*;
import com.example.backend.entity.userInfo.adminUserInfoRequest;
import com.example.backend.mapper.UserMapper;
import com.example.backend.services.AccessService;
import com.example.backend.services.AttendanceService;

import com.example.backend.services.UserService;
import com.example.backend.services.utils.IpUtil;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
    @Autowired
    private AccessService accessService;
    @Autowired
    private UserService userService;
    public String setStatus(Attendance record){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date=sdf.format(calendar.getTime());
        //data是今天的日期

        // 定义上班和下班的标准时间
        LocalTime workStartTime = LocalTime.of(9, 0); // 上班时间 9:00
        LocalTime workEndTime = LocalTime.of(18, 0);  // 下班时间 18:00

        // 判断迟到或早退状态
        if (record.getCheckIn() != null&&record.getCheckOut() != null) {
            if(record.getCheckOut().isAfter(workEndTime)&&record.getCheckIn().isAfter(workEndTime)) {
                return "Absent";
            }
            if(record.getCheckOut().isBefore(workStartTime)&&record.getCheckIn().isBefore(workStartTime)) {
                return "Absent";
            }
            if (record.getCheckIn().isAfter(workStartTime)&&record.getCheckOut().isAfter(workEndTime)) {
                return "Late"; // 迟到
            }
            if (record.getCheckOut().isBefore(workEndTime)&&record.getCheckIn().isBefore(workStartTime)) {
                return "Leave Early"; // 早退
            }
            if (record.getCheckOut().isAfter(workEndTime)&&record.getCheckIn().isBefore(workStartTime)) {
                return "On Time";
            }
            if (record.getCheckOut().isBefore(workEndTime)&&record.getCheckIn().isAfter(workStartTime)) {
                return "Late And Leave Early";
            }
        }
        // 如果没有考勤记录，设为缺席
        if (record.getCheckIn() == null && record.getCheckOut() == null) {
            return "Absent"; // 缺席
        }
        if (record.getCheckIn() != null && record.getCheckOut() == null) {
            if (record.getCheckIn().isAfter(workStartTime)) {
                return "Late"; // 迟到
            }
            if (record.getCheckIn().isBefore(workStartTime)) {
                return "On Time"; // 迟到
            }
        }
        return null;
    }



    @PostMapping("/loadAttendance")
    public ResponseBase loadAttendance(@RequestParam String pickDate) {
        ResponseBase res = new ResponseBase();
        System.out.println(pickDate);
        System.out.println("进入后端Attendance");
        if(pickDate==null|| pickDate.isEmpty()|| pickDate.equals("Invalid Date")){
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            pickDate=sdf.format(calendar.getTime());
            System.out.println(pickDate);
        }
        // 定义日期格式

        try {
            List<Attendance> records=attendanceService.getAttendanceRecord(pickDate);
            for (Attendance record : records) {
                User userInfo = userMapper.findByUserId(record.getUserId());
                record.setUserName(userInfo.getUsername());
                record.setDepartment(userInfo.getDepartment());
                record.setRole(userInfo.getRole());
                record.setStatus(setStatus(record));
                res.pushData(record);
            }
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

    @PostMapping("/personalAttendance")
    public ResponseEntity<ResponseBase> personalAttendance(@RequestParam String accessToken) {
        ResponseBase res = new ResponseBase();
        System.out.println("personalAttendance");
        int userId = accessService.getAuthenticatedId(accessToken);
        System.out.println(userId);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date=sdf.format(calendar.getTime());
        List<Attendance> records=attendanceService.getPersonalAttendanceRecord(userId);
        User userInfo = userMapper.findByUserId(userId);
        for (Attendance record : records) {
            String AttendanceDate= String.valueOf(record.getAttendanceDate());
            //if(record.getUserId()==userId&&AttendanceDate.equals(date)) {
            if(record.getUserId()==userId) {
                record.setUserName(userInfo.getUsername());
                record.setDepartment(userInfo.getDepartment());
                record.setRole(userInfo.getRole());
                record.setStatus(setStatus(record));
                res.pushData(record);
                System.out.println("id:"+record.getId());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
    @PostMapping("/checkInAttendance")
    public ResponseEntity<ResponseBase> checkInAttendance(@RequestParam String accessToken) throws UnknownHostException {
        ResponseBase res = new ResponseBase();
        System.out.println("checkInAttendance");
        int userId = accessService.getAuthenticatedId(accessToken);
        System.out.println(userId);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date=sdf.format(calendar.getTime());
        List<Attendance> records=attendanceService.getAttendanceRecord(date);
        for (Attendance record : records) {
            String AttendanceDate= String.valueOf(record.getAttendanceDate());
            if(record.getUserId()==userId&&AttendanceDate.equals(date)) {
                res.setStatus(-1);
                res.setMessage("You have already checked in today.");
                System.out.println("You have already checked in today.");
                return ResponseEntity.status(HttpStatus.OK).body(res);
            }
        }
        // 获取当前时间
        LocalTime currentTime = LocalTime.now();
        // 格式化时间为 "HH:mm:ss"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = currentTime.format(formatter);
        InetAddress addr = InetAddress.getLocalHost();
        System.out.println("Local HostAddress: "+addr.getHostAddress());
        String ipRegion = IpUtil.getIpRegion(addr.getHostAddress());
        System.out.println(ipRegion);

        Attendance record=new Attendance();
        record.setUserId(userId);
        record.setCheckIn(LocalTime.parse(formattedTime));
        record.setAttendanceDate(LocalDate.parse(date));
        record.setStatus(setStatus(record));
        record.setInLocation(ipRegion);

        int res_code=attendanceService.addAttendance(record);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @PostMapping("/checkOutAttendance")
    public ResponseEntity<ResponseBase> checkOutAttendance(@RequestParam String accessToken) throws UnknownHostException {
        ResponseBase res = new ResponseBase();
        Attendance attendance = new Attendance();
        System.out.println("checkOutAttendance");
        int userId = accessService.getAuthenticatedId(accessToken);
        System.out.println(userId);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date=sdf.format(calendar.getTime());
        List<Attendance> records=attendanceService.getAttendanceRecord(date);
        int is_exist=0;
        for (Attendance record : records) {
            String AttendanceDate= String.valueOf(record.getAttendanceDate());
            if(record.getUserId()==userId&&AttendanceDate.equals(date)) {
                if(record.getCheckOut()!=null) {
                    res.setStatus(-1);
                    res.setMessage("You have already checked out today.");
                    return ResponseEntity.status(HttpStatus.OK).body(res);
                }else{
                    is_exist=1;
                    attendance=record;
                    break;
                }
            }
        }
        if(is_exist==0) {
            res.setStatus(-2);
            res.setMessage("You have not checked in today.");
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        // 获取当前时间
        LocalTime currentTime = LocalTime.now();
        // 格式化时间为 "HH:mm:ss"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = currentTime.format(formatter);
        InetAddress addr = InetAddress.getLocalHost();
        System.out.println("Local HostAddress: "+addr.getHostAddress());
        String ipRegion = IpUtil.getIpRegion(addr.getHostAddress());
        System.out.println(ipRegion);

        attendance.setCheckOut(LocalTime.parse(formattedTime));
        attendance.setStatus(setStatus(attendance));
        attendance.setOutLocation(ipRegion);

        int res_code=attendanceService.editAttendance(attendance);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @PostMapping("/exportAttendance")
    public void exportAttendance(HttpServletResponse response, @RequestBody adminUserInfoRequest request) {
        try {
            String accessToken = request.getAccessToken();
            List<Integer> ids = request.getUser_ids();
            int userId = accessService.getAuthenticatedId(accessToken);
            String role = userService.getById(userId).getRole();
            List<Attendance> attendances=attendanceService.excelAttendance(ids);
            System.out.println(attendances.size());
            // Create an Excel Workbook
            Workbook workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet("考勤列表");

            // Create header row
            Row headerRow = sheet.createRow(0);
            if(role.equals("admin")) {
                headerRow.createCell(0).setCellValue("用户ID");
                headerRow.createCell(1).setCellValue("用户名");
                headerRow.createCell(2).setCellValue("部门");
                headerRow.createCell(3).setCellValue("职位");
                headerRow.createCell(4).setCellValue("上班打卡时间");
                headerRow.createCell(5).setCellValue("上班打卡位置");
                headerRow.createCell(6).setCellValue("下班打卡时间");
                headerRow.createCell(7).setCellValue("下班打卡位置");
                headerRow.createCell(8).setCellValue("考勤状态");
            }

            else{
                headerRow.createCell(0).setCellValue("日期");
                headerRow.createCell(1).setCellValue("上班打卡时间");
                headerRow.createCell(2).setCellValue("上班打卡位置");
                headerRow.createCell(3).setCellValue("下班打卡时间");
                headerRow.createCell(4).setCellValue("下班打卡位置");
                headerRow.createCell(5).setCellValue("考勤状态");
            }
            // Fill data into rows
            int rowNum = 1;
            if(role.equals("admin")){
                for (Attendance attendance : attendances) {
                    User userInfo = userMapper.findByUserId(attendance.getUserId());
                    attendance.setUserName(userInfo.getUsername());
                    attendance.setDepartment(userInfo.getDepartment());
                    attendance.setRole(userInfo.getRole());
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(attendance.getUserId());
                    row.createCell(1).setCellValue(attendance.getUserName());
                    row.createCell(2).setCellValue(attendance.getDepartment());
                    row.createCell(3).setCellValue(attendance.getRole());
                    String checkInTime = (attendance.getCheckIn() != null) ? attendance.getCheckIn().toString() : "未打卡";
                    row.createCell(4).setCellValue(checkInTime);
                    row.createCell(5).setCellValue(attendance.getInLocation());
                    String checkOutTime = (attendance.getCheckOut() != null) ? attendance.getCheckOut().toString() : "未打卡";
                    row.createCell(6).setCellValue(checkOutTime);
                    row.createCell(7).setCellValue(attendance.getOutLocation());
                    attendance.setStatus(setStatus(attendance));
                    row.createCell(8).setCellValue(attendance.getStatus());
                }
            }
            else{
                for (Attendance attendance : attendances) {
                    User userInfo = userMapper.findByUserId(attendance.getUserId());
                    attendance.setUserName(userInfo.getUsername());
                    attendance.setDepartment(userInfo.getDepartment());
                    attendance.setRole(userInfo.getRole());
                    Row row = sheet.createRow(rowNum++);
                    String attendanceDate = (attendance.getAttendanceDate() != null) ? attendance.getAttendanceDate().toString() : "未打卡";
                    row.createCell(0).setCellValue(attendanceDate);
                    String checkInTime = (attendance.getCheckIn() != null) ? attendance.getCheckIn().toString() : "未打卡";
                    row.createCell(1).setCellValue(checkInTime);
                    row.createCell(2).setCellValue(attendance.getInLocation());
                    String checkOutTime = (attendance.getCheckOut() != null) ? attendance.getCheckOut().toString() : "未打卡";
                    row.createCell(3).setCellValue(checkOutTime);
                    row.createCell(4).setCellValue(attendance.getOutLocation());
                    attendance.setStatus(setStatus(attendance));
                    row.createCell(5).setCellValue(attendance.getStatus());
                }
            }

            // Set response headers for file download
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;");

            // Write the workbook to the response output stream
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/printAttendance")
    public void printAttendance(HttpServletResponse response, @RequestBody adminUserInfoRequest request) {
        try {
            String accessToken = request.getAccessToken();
            List<Integer> ids = request.getUser_ids();
            int userId = accessService.getAuthenticatedId(accessToken);
            String role = userService.getById(userId).getRole();
            List<Attendance> attendances=attendanceService.excelAttendance(ids);
            Document document = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            document.open();

            BaseFont bfChinese = BaseFont.createFont("STSong-Light","UniGB-UTF16-H",BaseFont.NOT_EMBEDDED);
            Font font = new Font(bfChinese);
            font.setColor(BaseColor.BLACK);
            // 创建表格
            // 根据 role 决定列数
            int numberOfColumns = role.equals("admin") ? 9 :6;
            PdfPTable table = new PdfPTable(numberOfColumns);
            table.setWidthPercentage(100); // 表格宽度占页面 100%

            // 添加表头(admin和manager表头不一样)
            String[] headers = {};
            if(role.equals("admin")) {
                headers = new String[]{"用户ID", "用户名", "部门", "职位", "上班打卡时间", "上班打卡位置", "下班打卡时间", "下班打卡位置", "考勤状态"};
            }
            else{
                headers = new String[]{"日期", "上班打卡时间", "上班打卡位置", "下班打卡时间", "下班打卡位置", "考勤状态"};
            }
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Paragraph(header, font));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                table.addCell(cell);
            }
            // 添加内容（admin和manager打印出来的东西不一样）
            if(role.equals("admin")) {
                for (Attendance attendance : attendances) {
                    User userInfo = userMapper.findByUserId(attendance.getUserId());
                    attendance.setUserName(userInfo.getUsername());
                    attendance.setDepartment(userInfo.getDepartment());
                    attendance.setRole(userInfo.getRole());
                    String checkInTime = (attendance.getCheckIn() != null) ? attendance.getCheckIn().toString() : "未打卡";
                    String checkOutTime = (attendance.getCheckOut() != null) ? attendance.getCheckOut().toString() : "未打卡";
                    table.addCell(new Paragraph(Integer.toString(attendance.getUserId()), font));
                    table.addCell(new Paragraph(attendance.getUserName(), font));
                    table.addCell(new Paragraph(attendance.getDepartment(), font));
                    table.addCell(new Paragraph(attendance.getRole(), font));
                    table.addCell(new Paragraph(checkInTime, font));
                    table.addCell(new Paragraph(attendance.getInLocation(), font));
                    table.addCell(new Paragraph(checkOutTime, font));
                    table.addCell(new Paragraph(attendance.getOutLocation(), font));
                    attendance.setStatus(setStatus(attendance));
                    table.addCell(new Paragraph(attendance.getStatus(), font));
                }
            }
            else{
                for (Attendance attendance : attendances) {
                    String checkInTime = (attendance.getCheckIn() != null) ? attendance.getCheckIn().toString() : "未打卡";
                    String checkOutTime = (attendance.getCheckOut() != null) ? attendance.getCheckOut().toString() : "未打卡";
                    String attendanceDate = (attendance.getAttendanceDate() != null) ? attendance.getAttendanceDate().toString() : "未打卡";
                    table.addCell(new Paragraph(attendanceDate, font));
                    table.addCell(new Paragraph(checkInTime, font));
                    table.addCell(new Paragraph(attendance.getInLocation(), font));
                    table.addCell(new Paragraph(checkOutTime, font));
                    table.addCell(new Paragraph(attendance.getOutLocation(), font));
                    attendance.setStatus(setStatus(attendance));
                    table.addCell(new Paragraph(attendance.getStatus(), font));
                }
            }
            // 将表格添加到文档
            document.add(table);
            document.close();
            // 设置响应头
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment;");

            // 将 PDF 内容写入响应
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }




}
