package com.example.backend.controllers;

import com.example.backend.entity.meeting.FieldKeyWithToken;
import com.example.backend.entity.meeting.Meeting;
import com.example.backend.entity.meeting.MeetingWithMultiUsers;
import com.example.backend.entity.todoList.TodoRecord;
import com.example.backend.entity.todoList.TodoRecordWithMultiUsers;
import com.example.backend.entity.todoList.TodoRecordWithTk;
import com.example.backend.entity.ResponseBase;
import com.example.backend.entity.userInfo.adminUserInfoRequest;
import com.example.backend.services.AccessService;
import com.itextpdf.text.Font;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.backend.services.TodoService;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

@RestController
@RequestMapping("/api/todolist")
public class TodoListCon {

    @Autowired
    private TodoService my_service;

    @Autowired
    private AccessService accessService;

    @PostMapping("/getRec")
    public ResponseBase getRec(@RequestBody adminUserInfoRequest request) {
//        System.out.println("[getRec] receive");
        ResponseBase res = new ResponseBase();

        try {
            String accessToken = request.getAccessToken();
            int userId = accessService.getAuthenticatedId(accessToken);
            List<TodoRecord> records = my_service.getRecordsByUserId(userId);

            for (TodoRecord record : records) {
                res.pushData(record);
            }
        }

        catch (Exception e) {
            res.setStatus(-1);
            res.setMessage(e.getMessage());
        }
        return res;
    }

    @PostMapping("/searchByFieldKey")
    public ResponseBase searchByFieldKey(@RequestBody FieldKeyWithToken request) {
        ResponseBase res = new ResponseBase();

        try {
            String accessToken = request.getAccessToken();
            int userId = accessService.getAuthenticatedId(accessToken);
            List<TodoRecord> records = my_service.searchByAnything(request.getField(), request.getKey(), userId);

            for (TodoRecord record : records) {
                res.pushData(record);
            }
            if (records.isEmpty()) {
//                System.out.println("result set is empty");
                res.setStatus(1);
            }
        }
        catch (Exception e) {
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
            List<TodoRecord> records = my_service.getRecordsByUserId(userId);

            Document document = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            document.open();

            BaseFont bfChinese = BaseFont.createFont("STSong-Light","UniGB-UTF16-H",BaseFont.NOT_EMBEDDED);
            Font font = new Font(bfChinese);
            font.setColor(BaseColor.BLACK);
            // 创建表格
            PdfPTable table = new PdfPTable(5); // 5 列
            table.setWidthPercentage(100); // 表格宽度占页面 100%

            // 添加表头
            String[] headers = {"ID", "待办事项名称", "待办事项内容", "待办事项截止日期", "待办事项创建时间"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Paragraph(header, font));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                table.addCell(cell);
            }

            // 添加内容（根据 records 数据添加）
            for (TodoRecord record : records) {
                table.addCell(new Paragraph(Integer.toString(record.getTodo_id()), font));
                table.addCell(new Paragraph(record.getTodo_title(), font));
                table.addCell(new Paragraph(record.getTodo_ctnt(), font));
                table.addCell(new Paragraph(record.getTodo_ddl(), font));
                table.addCell(new Paragraph(record.getTodo_crt(), font));
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
            List<TodoRecord> records = my_service.getRecordsByUserId(userId);

            // Create an Excel Workbook
            Workbook workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet("待办事项");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("待办事项ID");
            headerRow.createCell(1).setCellValue("待办事项标题");
            headerRow.createCell(2).setCellValue("待办事项内容");
            headerRow.createCell(3).setCellValue("截止日期");

            // Fill data into rows
            int rowNum = 1;
            for (TodoRecord record : records) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(record.getTodo_id());
                row.createCell(1).setCellValue(record.getTodo_title());
                row.createCell(2).setCellValue(record.getTodo_ctnt());
                row.createCell(3).setCellValue(record.getTodo_ddl());
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

    @PostMapping("/modifyRec")
    public ResponseBase modifyRec(@RequestBody TodoRecord record) {
        int res_code = my_service.updateTodoRecord(record);
//        System.out.println("modifyRec res_code: " + res_code);
        return new ResponseBase();
    }

    @PostMapping("/add")
    public ResponseBase addRec(@RequestBody TodoRecordWithTk request) {
        ResponseBase res = new ResponseBase();
        int res_code = -1;
        try {
            String accessToken = request.getAcsTkn();
            System.out.println(accessToken);
            int userId = accessService.getAuthenticatedId(accessToken);
            request.setAdder_id(userId);
            request.setUser_id(userId);
            res_code = my_service.insertTodoRecord(request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            res.setStatus(-1);
            res.setMessage(e.getMessage());
        }
        System.out.println("addRec res_code: " + res_code);
        return res;
    }

    @PostMapping("/getTodoStatistics")
    public ResponseBase getTodoStatistics() {
        HashMap<String, Integer> statistics = new HashMap<>();
        ResponseBase res = new ResponseBase();

        statistics.put("自己布置未完成", my_service.getUnfinSelf());
        statistics.put("自己布置已完成", my_service.getFinishedSelf());
        statistics.put("上级布置已完成", my_service.getFinishedMana());
        statistics.put("上级布置未完成", my_service.getUnfinMana());
        res.pushData(my_service.getTotal());
        res.pushData(statistics);
        return res;
    }

    @PostMapping("/deleteTodo")
    public ResponseBase deleteRec(@RequestBody TodoRecord record) {
        System.out.println("deleteRecord " + record.getTodo_id());
        int res_code = my_service.deleteRecord(record);
        System.out.println("deleteRecord res_code: " + res_code);
        return new ResponseBase();
    }

    @PostMapping("/distributed_create")
    public ResponseBase distributedCreateMeeting(@RequestBody TodoRecordWithMultiUsers metaRecord) {
        ResponseBase res = new ResponseBase();
        try {
            String accessToken = metaRecord.getAccessToken();
            int adder_id = accessService.getAuthenticatedId(accessToken);

            TodoRecordWithTk this_record = new TodoRecordWithTk (
                    -1, -1, adder_id, metaRecord.getTodo_title(),
                        metaRecord.getTodo_ctnt(), metaRecord.getTodo_fin(),
                    metaRecord.getTodo_ddl(), null, null
                    );

            for (int user_id: metaRecord.getUser_ids()) {
                this_record.setUser_id(user_id);
                my_service.insertTodoRecord(this_record);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            res.setStatus(-1);
            res.setMessage(e.getMessage());
        }
        return res;
    }
}
