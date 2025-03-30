package com.example.backend.controllers;

import com.example.backend.entity.Department;
import com.example.backend.entity.User;
import com.example.backend.entity.authedRoutes.AuthedRoutesRequest;
import com.example.backend.entity.authedRoutes.AuthedRoutesResponse;
import com.example.backend.entity.department.adminDepartmentInfoRequest;
import com.example.backend.entity.department.adminDepartmentInfoResponse;
import com.example.backend.entity.userInfo.adminUserInfoRequest;
import com.example.backend.entity.userInfo.adminUserInfoResponse;
import com.example.backend.entity.userInfo.userInfoResponse;
import com.example.backend.entity.userInfo.userStatisticResponse;
import com.example.backend.services.AccessService;
import com.example.backend.services.DepartmentService;
import com.example.backend.services.MenuService;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class Admin {
    @Autowired
    private UserService userService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private AccessService accessService;
    @Autowired
    private MenuService menuService;
    @PostMapping("/department/list")
    public ResponseEntity<adminDepartmentInfoResponse> departmentList(@RequestBody adminDepartmentInfoRequest request) {
        try {
            String accessToken = request.getAccessToken();
            int userId = accessService.getAuthenticatedId(accessToken);
            List<Department> departmentInfo = departmentService.adminDepartmentInfo(userId);
            List<adminDepartmentInfoResponse.Data> data = new ArrayList<>();
            for(Department department:departmentInfo){
                adminDepartmentInfoResponse.Data temp = new adminDepartmentInfoResponse.Data();
                temp.setDepartmentId(department.getDepartmentId());
                temp.setDepartmentName(department.getDepartmentName());
                data.add(temp);
            }
            adminDepartmentInfoResponse response = new adminDepartmentInfoResponse(
                    0,
                    "获取部门列表成功",
                    true,
                    data
            );
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (Exception e) {
            adminDepartmentInfoResponse response = new adminDepartmentInfoResponse(
                    1,
                    "服务器内部错误",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/department/add")
    public ResponseEntity<adminDepartmentInfoResponse> departmentAdd(@RequestBody adminDepartmentInfoRequest request) {
        try {
            String accessToken = request.getAccessToken();
            String departmentName = request.getDepartmentName();
            int userId = accessService.getAuthenticatedId(accessToken);
            int isSuccess = departmentService.adminDepartmentAdd(userId,departmentName);
            if(isSuccess > 0) {
                adminDepartmentInfoResponse response = new adminDepartmentInfoResponse(
                        0,
                        "获取部门列表成功",
                        true,
                        null
                );
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            else{
                adminDepartmentInfoResponse response = new adminDepartmentInfoResponse(
                        1,
                        "权限不足",
                        false,
                        null
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        }
        catch (Exception e) {
            adminDepartmentInfoResponse response = new adminDepartmentInfoResponse(
                    2,
                    "服务器内部错误",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/department/delete")
    public ResponseEntity<adminDepartmentInfoResponse> departmentDelete(@RequestBody adminDepartmentInfoRequest request) {
        try {
            String accessToken = request.getAccessToken();
            int departmentId = request.getDepartmentId();
            int userId = accessService.getAuthenticatedId(accessToken);
            int isSuccess = departmentService.adminDepartmentDelete(userId,departmentId);
            if(isSuccess > 0) {
                adminDepartmentInfoResponse response = new adminDepartmentInfoResponse(
                        0,
                        "获取部门列表成功",
                        true,
                        null
                );
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            else{
                adminDepartmentInfoResponse response = new adminDepartmentInfoResponse(
                        1,
                        "权限不足",
                        false,
                        null
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        }
        catch (Exception e) {
            adminDepartmentInfoResponse response = new adminDepartmentInfoResponse(
                    2,
                    "服务器内部错误",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/user/list")
    public ResponseEntity<adminUserInfoResponse> userList(@RequestBody adminUserInfoRequest request) {
        try {
            String accessToken = request.getAccessToken();
            int userId = accessService.getAuthenticatedId(accessToken);
            List<User> userInfo = userService.adminUserInfo(userId);
            List<adminUserInfoResponse.Data> data = new ArrayList<>();
            for(User user:userInfo){
                adminUserInfoResponse.Data temp = new adminUserInfoResponse.Data();
                temp.setUserId(user.getUserId());
                temp.setUserName(user.getUsername());
                temp.setUserDepartment(user.getDepartment());
                temp.setUserEmail(user.getEmail());
                temp.setUserRole(user.getRole());
                temp.setUserPhone(user.getPhone());
                temp.setUserAvatar(userService.userInfoAvatar(user.getUserId()));
                data.add(temp);
            }
            adminUserInfoResponse response = new adminUserInfoResponse(
                    0,
                    "获取用户列表成功",
                    true,
                    data
            );
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (Exception e) {
            adminUserInfoResponse response = new adminUserInfoResponse(
                    1,
                    "服务器内部错误",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/user/add")
    public ResponseEntity<adminUserInfoResponse> userAdd(@RequestBody adminUserInfoRequest request) {
        try {
            String accessToken = request.getAccessToken();
            int adminId = accessService.getAuthenticatedId(accessToken);
            int departmentId = request.getDepartmentId();
            String username = request.getUserName();
            String userdepartment = request.getUserDepartment();
            String userrole = request.getUserRole();
            int userId = userService.adminUserAdd(username, userdepartment, userrole, adminId);
            int isSuccess =departmentService.adminUserDepartmentBind(userId, departmentId);
            if (isSuccess > 0) {
                adminUserInfoResponse response = new adminUserInfoResponse(
                        0,
                        "添加用户成功",
                        true,
                        null
                );
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            else{
                adminUserInfoResponse response = new adminUserInfoResponse(
                        1,
                        "添加用户失败",
                        false,
                        null
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        }
        catch (Exception e) {
            adminUserInfoResponse response = new adminUserInfoResponse(
                    1,
                    "服务器内部错误",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/user/update")
    public ResponseEntity<adminUserInfoResponse> userUpdate(@RequestBody adminUserInfoRequest request) {
        try {
            String accessToken = request.getAccessToken();
            String username = request.getUserName();
            String userdepartment = request.getUserDepartment();
            String userrole = request.getUserRole();
            int userId = request.getUserId();
            int adminId = accessService.getAuthenticatedId(accessToken);
            int isSuccess = userService.adminUserUpdate(username, userdepartment, userrole, adminId, userId);
            if (isSuccess > 0) {
                adminUserInfoResponse response = new adminUserInfoResponse(
                        0,
                        "更新用户信息成功",
                        true,
                        null
                );
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            else{
                adminUserInfoResponse response = new adminUserInfoResponse(
                        1,
                        "更新用户信息失败",
                        false,
                        null
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        }
        catch (Exception e) {
            adminUserInfoResponse response = new adminUserInfoResponse(
                    2,
                    "服务器内部错误",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/user/delete")
    public ResponseEntity<adminUserInfoResponse> userDelete(@RequestBody adminUserInfoRequest request) {
        try {
            String accessToken = request.getAccessToken();
            int userId = request.getUserId();
            int adminId = accessService.getAuthenticatedId(accessToken);
            int isSuccess = departmentService.adminUserDepartmentUnbind(userId);
            isSuccess = userService.adminUserDelete(adminId, userId);
            if (isSuccess > 0) {
                adminUserInfoResponse response = new adminUserInfoResponse(
                        0,
                        "获取用户列表成功",
                        true,
                        null
                );
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            else{
                adminUserInfoResponse response = new adminUserInfoResponse(
                        1,
                        "删除用户失败",
                        false,
                        null
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        }
        catch (Exception e) {
            adminUserInfoResponse response = new adminUserInfoResponse(
                    2,
                    "服务器内部错误",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/user/detail")
    public ResponseEntity<userInfoResponse> userDetail(@RequestBody adminUserInfoRequest request) {
        try {
            String accessToken = request.getAccessToken();
            int user_id = request.getUserId();
            int adminId = accessService.getAuthenticatedId(accessToken);
            String role = userService.getById(adminId).getRole();
            if(role.equals("admin")||role.equals("manager")) {
                User userInfo = userService.userInfo(user_id);
                String avatar = userService.userInfoAvatar(user_id);
                if (userInfo != null) {
                    userInfoResponse.Data userInfoResponseData = new userInfoResponse.Data();
                    userInfoResponseData.setUserEmail(userInfo.getEmail());
                    userInfoResponseData.setRoleName(userInfo.getRole());
                    userInfoResponseData.setUserName(userInfo.getUsername());
                    userInfoResponseData.setUserIntro(userInfo.getIntro());
                    userInfoResponseData.setUserPhone(userInfo.getPhone());
                    userInfoResponseData.setUserDepartment(userInfo.getDepartment());
                    userInfoResponseData.setUserAvatar(avatar);
                    userInfoResponse response = new userInfoResponse(
                            0,
                            "获取职位信息成功",
                            true,
                            userInfoResponseData
                    );
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    userInfoResponse response = new userInfoResponse(
                            1,
                            "更新用户信息失败",
                            false,
                            null
                    );
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
            }
            else{
                userInfoResponse response = new userInfoResponse(
                        1,
                        "权限不足",
                        false,
                        null
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            userInfoResponse response = new userInfoResponse(
                    2,
                    "服务器内部错误",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/user/export")
    public void userExport(HttpServletResponse response, @RequestBody adminUserInfoRequest request) {
        try {
            String accessToken = request.getAccessToken();
            List<Integer> user_ids = request.getUser_ids();
            int userId = accessService.getAuthenticatedId(accessToken);
            String role = userService.getById(userId).getRole();
            List<User> users = userService.listByIds(user_ids);

            // Create an Excel Workbook
            Workbook workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet("用户列表");

            // Create header row
            Row headerRow = sheet.createRow(0);
            if(role.equals("admin")) {
                headerRow.createCell(0).setCellValue("用户ID");
                headerRow.createCell(1).setCellValue("用户名");
                headerRow.createCell(2).setCellValue("部门");
                headerRow.createCell(3).setCellValue("职位");
                headerRow.createCell(4).setCellValue("电话号码");
            }
            else if(role.equals("manager")){
                headerRow.createCell(0).setCellValue("用户名");
                headerRow.createCell(1).setCellValue("部门");
                headerRow.createCell(2).setCellValue("职位");
                headerRow.createCell(3).setCellValue("电话号码");
            }
            else{}
            // Fill data into rows
            int rowNum = 1;
            if(role.equals("admin")){
                for (User user : users) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(user.getUserId());
                    row.createCell(1).setCellValue(user.getUsername());
                    row.createCell(2).setCellValue(user.getDepartment());
                    row.createCell(3).setCellValue(user.getRole());
                    row.createCell(4).setCellValue(user.getPhone());
                }
            }
            else if(role.equals("manager")){
                for (User user : users) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(user.getUsername());
                    row.createCell(1).setCellValue(user.getDepartment());
                    row.createCell(2).setCellValue(user.getRole());
                    row.createCell(3).setCellValue(user.getPhone());
                }
            }
            else{}
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
    @PostMapping("/user/download")
    public void userDownload(HttpServletResponse response, @RequestBody adminUserInfoRequest request) {
        try {
            String accessToken = request.getAccessToken();
            List<Integer> user_ids = request.getUser_ids();
            int userId = accessService.getAuthenticatedId(accessToken);
            String role = userService.getById(userId).getRole();
            List<User> users = userService.listByIds(user_ids);
            Document document = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            document.open();

            BaseFont bfChinese = BaseFont.createFont("STSong-Light","UniGB-UTF16-H",BaseFont.NOT_EMBEDDED);
            Font font = new Font(bfChinese);
            font.setColor(BaseColor.BLACK);
            // 创建表格
            // 根据 role 决定列数
            int numberOfColumns = role.equals("admin") ? 5 : 4;
            PdfPTable table = new PdfPTable(numberOfColumns);
            table.setWidthPercentage(100); // 表格宽度占页面 100%

            // 添加表头(admin和manager表头不一样)
            String[] headers = {};
            if(role.equals("admin")) {
                headers = new String[]{"用户ID", "用户名", "部门", "职位", "电话号码"};
            }
            else if(role.equals("manager")){
                headers = new String[]{"用户名","部门", "职位", "电话号码"};
            }
            else{headers=new String[]{};}//处理不规范用法
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Paragraph(header, font));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                table.addCell(cell);
            }
            // 添加内容（admin和manager打印出来的东西不一样）
            if(role.equals("admin")) {
                for (User user : users) {
                    table.addCell(new Paragraph(Integer.toString(user.getUserId()), font));
                    table.addCell(new Paragraph(user.getUsername(), font));
                    table.addCell(new Paragraph(user.getDepartment(), font));
                    table.addCell(new Paragraph(user.getRole(), font));
                    table.addCell(new Paragraph(user.getPhone(), font));
                }
            }
            else if(role.equals("manager")){
                for (User user : users) {
                    table.addCell(new Paragraph(user.getUsername(), font));
                    table.addCell(new Paragraph(user.getDepartment(), font));
                    table.addCell(new Paragraph(user.getRole(), font));
                    table.addCell(new Paragraph(user.getPhone(), font));
                }
            }
            else{}
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
    @PostMapping("/user/statistic")
    public ResponseEntity<userStatisticResponse> userStatistic(@RequestBody adminUserInfoRequest request) {
        try {
            String accessToken = request.getAccessToken();
            int userId = accessService.getAuthenticatedId(accessToken);
            String role = userService.getById(userId).getRole();
            if(role.equals("admin")) {
                List<User> userInfo = userService.adminUserInfo(userId);
                Integer onlineUsers = accessService.getOnlineUsers();
                Map<String, Long> userRoleCounts = userInfo.stream()
                        .collect(Collectors.groupingBy(User::getRole, Collectors.counting()));
                List<userStatisticResponse.Data> data = userRoleCounts.entrySet().stream()
                        .map(entry -> {
                            userStatisticResponse.Data dataEntry = new userStatisticResponse.Data();
                            dataEntry.setName(entry.getKey());
                            dataEntry.setValue(entry.getValue().intValue());
                            return dataEntry;
                        }).toList();
                userStatisticResponse response = new userStatisticResponse(
                        0,
                        "获取用户统计信息成功",
                        true,
                        onlineUsers,
                        data
                );
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            else{
                userStatisticResponse response = new userStatisticResponse(
                        1,
                        "权限不足",
                        false,
                        0,
                        null
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        }
        catch (Exception e) {
            userStatisticResponse response = new userStatisticResponse(
                    2,
                    "获取用户统计信息失败",
                    false,
                    0,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/menu/update")
    public ResponseEntity<AuthedRoutesResponse> menuUpdate(@RequestBody AuthedRoutesRequest request) {
        try {
            String permissions = request.getAuthedRoutes().toString();
            String accessToken = request.getAccessToken();
            int userId = request.getUserId();
            int adminId = accessService.getAuthenticatedId(accessToken);
            int isSuccess = menuService.updateMenu(userId,adminId,permissions);
            if (isSuccess > 0) {
                AuthedRoutesResponse response = new AuthedRoutesResponse(
                        0,
                        "修改用户授权菜单成功",
                        true,
                        null
                );
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            else{
                AuthedRoutesResponse response = new AuthedRoutesResponse(
                        1,
                        "修改用户授权菜单失败",
                        false,
                        null
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        }
        catch (Exception e) {
            AuthedRoutesResponse response = new AuthedRoutesResponse(
                    2,
                    "服务器内部错误",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/wechat/routes")
    public ResponseEntity<AuthedRoutesResponse> wechatAutheredRoutes(@RequestBody AuthedRoutesRequest request) {
        //获取邮箱
        try{
            String accessToken = request.getAccessToken();
            int adminId = accessService.getAuthenticatedId(accessToken);
            int user_id = request.getUserId();
            String admin = userService.getById(adminId).getRole();
            String roleName = userService.getById(user_id).getRole();
            if (admin.equals("admin")) {
                if (roleName.equals("admin")) {
                    AuthedRoutesResponse.Data data = new AuthedRoutesResponse.Data();
                    List<String> authedRoutes = Arrays.asList(
                            "user","worker", "todo","meeting","reimbursement","leave","file","attendance"
                    );
                    data.setAuthedRoutes(authedRoutes);
                    AuthedRoutesResponse response = new AuthedRoutesResponse(
                            0,
                            "获取职位信息成功",
                            true,
                            data
                    );
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else if (roleName.equals("manager")) {
                    AuthedRoutesResponse.Data data = new AuthedRoutesResponse.Data();
                    List<String> authedRoutes = Arrays.asList(
                            "worker", "todo","meeting","reimbursement","leave","file","attendance"
                    );
                    data.setAuthedRoutes(authedRoutes);
                    AuthedRoutesResponse response = new AuthedRoutesResponse(
                            0,
                            "获取职位信息成功",
                            true,
                            data
                    );
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    AuthedRoutesResponse.Data data = new AuthedRoutesResponse.Data();
                    List<String> authedRoutes = Arrays.asList(
                            "todo","meeting","reimbursement","leave","file","attendance");
                    data.setAuthedRoutes(authedRoutes);
                    AuthedRoutesResponse response = new AuthedRoutesResponse(
                            0,
                            "获取职位信息成功",
                            true,
                            data
                    );
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
            } else {
                // 构建失败响应
                AuthedRoutesResponse response = new AuthedRoutesResponse(
                        1,
                        "获取职位信息失败",
                        false,
                        null
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        }catch (Exception e) {
            AuthedRoutesResponse response = new AuthedRoutesResponse(
                    2,
                    "服务器内部错误",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
