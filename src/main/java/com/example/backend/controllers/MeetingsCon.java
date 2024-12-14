package com.example.backend.controllers;

import com.example.backend.entity.ResponseBase;
import com.example.backend.entity.meeting.*;
import com.example.backend.entity.todoList.TodoRecord;
import com.example.backend.entity.userInfo.adminUserInfoRequest;
import com.example.backend.services.AccessService;
import com.example.backend.services.MeetingService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
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

    @PostMapping("/getPdf")
    public void getPdf(HttpServletResponse response, @RequestBody adminUserInfoRequest request) {
        try {
            String accessToken = request.getAccessToken();
            int userId = accessService.getAuthenticatedId(accessToken);
            ResponseBase res = meetingService.getPersonalMeetings(userId);

            Document document = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            document.open();

            BaseFont bfChinese = BaseFont.createFont("STSong-Light","UniGB-UTF16-H",BaseFont.NOT_EMBEDDED);
            Font font = new Font(bfChinese);
            font.setColor(BaseColor.BLACK);
            // 创建表格
            PdfPTable table = new PdfPTable(6); // 5 列
            table.setWidthPercentage(100); // 表格宽度占页面 100%

            // 添加表头
            String[] headers = {"会议ID", "会议名称", "会议简介", "会议时间", "会议长度", "会议状态"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Paragraph(header, font));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                table.addCell(cell);
            }

            var data = res.getData();
            List<MeetingWithAdderId> scheduled = (List<MeetingWithAdderId>) data.get(0);
            List<MeetingWithAdderId> processing = (List<MeetingWithAdderId>) data.get(1);
            List<MeetingWithAdderId> passed  = (List<MeetingWithAdderId>) data.get(2);

            // 添加内容（根据 records 数据添加）
            for (MeetingWithAdderId record :scheduled) {
                table.addCell(new Paragraph(Integer.toString(record.getMtin_id()), font));
                table.addCell(new Paragraph(record.getMtin_title(), font));
                table.addCell(new Paragraph(record.getMtin_ctnt(), font));
                table.addCell(new Paragraph(record.getMtin_st(), font));
                table.addCell(new Paragraph(record.getMtin_len(), font));
                table.addCell(new Paragraph("已预订", font));
            }

            for (MeetingWithAdderId record :processing) {
                table.addCell(new Paragraph(Integer.toString(record.getMtin_id()), font));
                table.addCell(new Paragraph(record.getMtin_title(), font));
                table.addCell(new Paragraph(record.getMtin_ctnt(), font));
                table.addCell(new Paragraph(record.getMtin_st(), font));
                table.addCell(new Paragraph(record.getMtin_len(), font));
                table.addCell(new Paragraph("今天会议", font));
            }

            for (MeetingWithAdderId record :passed) {
                table.addCell(new Paragraph(Integer.toString(record.getMtin_id()), font));
                table.addCell(new Paragraph(record.getMtin_title(), font));
                table.addCell(new Paragraph(record.getMtin_ctnt(), font));
                table.addCell(new Paragraph(record.getMtin_st(), font));
                table.addCell(new Paragraph(record.getMtin_len(), font));
                table.addCell(new Paragraph("已过期", font));
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

    @PostMapping("/getExcel")
    public void getExcel(HttpServletResponse response, @RequestBody adminUserInfoRequest request) {
        try {
            String accessToken = request.getAccessToken();
            int userId = accessService.getAuthenticatedId(accessToken);
            ResponseBase res = meetingService.getPersonalMeetings(userId);

            // Create an Excel Workbook
            Workbook workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet("我的会议");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("会议ID");
            headerRow.createCell(1).setCellValue("会议名称");
            headerRow.createCell(2).setCellValue("会议简介");
            headerRow.createCell(3).setCellValue("会议时间");
            headerRow.createCell(4).setCellValue("会议长度");
            headerRow.createCell(5).setCellValue("会议状态");


            ArrayList<Object> data = res.getData();
            List<MeetingWithAdderId> scheduled = (List<MeetingWithAdderId>) data.get(0);
            List<MeetingWithAdderId> processing = (List<MeetingWithAdderId>) data.get(1);
            List<MeetingWithAdderId> passed  = (List<MeetingWithAdderId>) data.get(2);
            // Fill data into rows
            int rowNum = 1;
            for (MeetingWithAdderId record : scheduled) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(record.getMtin_id());
                row.createCell(1).setCellValue(record.getMtin_title());
                row.createCell(2).setCellValue(record.getMtin_ctnt());
                row.createCell(3).setCellValue(record.getMtin_st());
                row.createCell(4).setCellValue(record.getMtin_len());
                row.createCell(5).setCellValue("已预订");
            }

            for (MeetingWithAdderId record : processing) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(record.getMtin_id());
                row.createCell(1).setCellValue(record.getMtin_title());
                row.createCell(2).setCellValue(record.getMtin_ctnt());
                row.createCell(3).setCellValue(record.getMtin_st());
                row.createCell(4).setCellValue(record.getMtin_len());
                row.createCell(5).setCellValue("今天会议");
            }

            for (MeetingWithAdderId record : passed) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(record.getMtin_id());
                row.createCell(1).setCellValue(record.getMtin_title());
                row.createCell(2).setCellValue(record.getMtin_ctnt());
                row.createCell(3).setCellValue(record.getMtin_st());
                row.createCell(4).setCellValue(record.getMtin_len());
                row.createCell(5).setCellValue("已过期");
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
