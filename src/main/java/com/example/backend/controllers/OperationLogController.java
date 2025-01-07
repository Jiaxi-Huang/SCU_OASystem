package com.example.backend.controllers;

import com.example.backend.entity.OperationLog;
import com.example.backend.entity.log.LogRequest;
import com.example.backend.entity.log.LogResponse;
import com.example.backend.services.AccessService;
import com.example.backend.services.OperationLogService;
import com.example.backend.services.UserService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/log")
public class OperationLogController {
    @Autowired
    private UserService userService;
    @Autowired
    private AccessService accessService;
    @Autowired
    private OperationLogService logService;
    @RequestMapping("/list")
    public ResponseEntity<LogResponse> userList(@RequestBody LogRequest request) {
        try {
            String accessToken = request.getAccessToken();
            int userId = accessService.getAuthenticatedId(accessToken);
            String role = userService.getById(userId).getRole();
            if(role.equals("admin")) {
                List<OperationLog> logs = logService.logList(userId);
                List<LogResponse.Data> data = new ArrayList<>();
                for (OperationLog log : logs) {
                    LogResponse.Data temp = new LogResponse.Data();
                    temp.setId(log.getId());
                    temp.setUserId(log.getUser_id());
                    temp.setLogContent(log.getContent());
                    temp.setLogDate(log.getDate());
                    data.add(temp);
                }
                LogResponse response = new LogResponse(
                        0,
                        "获取操作日志成功",
                        true,
                        data
                );
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            else{
                LogResponse response = new LogResponse(
                        1,
                        "权限不足",
                        false,
                        null
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        }
        catch (Exception e){
            LogResponse response = new LogResponse(
                    2,
                    "服务器内部错误",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @RequestMapping("/delete")
    public ResponseEntity<LogResponse> userDelete(@RequestBody LogRequest request) {
        try {
            String accessToken = request.getAccessToken();
            List<Integer> ids = request.getIds();
            int userId = accessService.getAuthenticatedId(accessToken);
            String role = userService.getById(userId).getRole();
            if(role.equals("admin")) {
                int isSuccess = logService.logDelete(userId,ids);
                if(isSuccess>0) {
                    LogResponse response = new LogResponse(
                            0,
                            "获取操作日志成功",
                            true,
                            null
                    );
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
                else{
                    LogResponse response = new LogResponse(
                            0,
                            "获取操作日志失败",
                            true,
                            null
                    );
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
            }
            else{
                LogResponse response = new LogResponse(
                        1,
                        "权限不足",
                        false,
                        null
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        }
        catch (Exception e){
            LogResponse response = new LogResponse(
                    2,
                    "服务器内部错误",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/export")
    public void logExport(HttpServletResponse response, @RequestBody LogRequest request) {
        try {
            String accessToken = request.getAccessToken();
            int userId = accessService.getAuthenticatedId(accessToken);
            String role = userService.getById(userId).getRole();
            List<OperationLog> logList = logService.logList(userId);
            // Create an Excel Workbook
            Workbook workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet("操作日志");
            // Create header row
            Row headerRow = sheet.createRow(0);
            if(role.equals("admin")) {
                headerRow.createCell(0).setCellValue("操作ID");
                headerRow.createCell(1).setCellValue("用户");
                headerRow.createCell(2).setCellValue("操作内容");
                headerRow.createCell(3).setCellValue("操作时间");
            }
            // Fill data into rows
            int rowNum = 1;
            if(role.equals("admin")){
                for (OperationLog log : logList) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(log.getId());
                    row.createCell(1).setCellValue(log.getUser_id());
                    row.createCell(2).setCellValue(log.getContent());
                    row.createCell(3).setCellValue(log.getDate());
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
    @PostMapping("/download")
    public void userDownload(HttpServletResponse response, @RequestBody LogRequest request) {
        try {
            String accessToken = request.getAccessToken();
            int userId = accessService.getAuthenticatedId(accessToken);
            String role = userService.getById(userId).getRole();
            List<OperationLog> logList = logService.logList(userId);
            Document document = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            document.open();

            BaseFont bfChinese = BaseFont.createFont("STSong-Light","UniGB-UTF16-H",BaseFont.NOT_EMBEDDED);
            Font font = new Font(bfChinese);
            font.setColor(BaseColor.BLACK);
            // 创建表格
            // 根据 role 决定列数
            int numberOfColumns = 4;
            PdfPTable table = new PdfPTable(numberOfColumns);
            table.setWidthPercentage(100); // 表格宽度占页面 100%

            // 添加表头(admin和manager表头不一样)
            String[] headers = {};
            if(role.equals("admin")) {
                headers = new String[]{"操作ID","用户ID", "操作内容", "时间"};
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
                for (OperationLog log : logList) {
                    table.addCell(new Paragraph(Integer.toString(log.getId()), font));
                    table.addCell(new Paragraph(Integer.toString(log.getUser_id()), font));
                    table.addCell(new Paragraph(log.getContent(), font));
                    table.addCell(new Paragraph(log.getDate(), font));
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
