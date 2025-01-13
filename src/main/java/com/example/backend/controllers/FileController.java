package com.example.backend.controllers;

import com.example.backend.entity.Files;
import com.example.backend.entity.ResponseBase;
import com.example.backend.entity.User;
import com.example.backend.entity.userInfo.adminUserInfoRequest;
import com.example.backend.mapper.UserMapper;
import com.example.backend.services.AccessService;
import com.example.backend.services.FileService;
import com.example.backend.services.FolderService;
import com.example.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private AccessService accessService;

    @Autowired
    UserService userService;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FolderController folderController;

    @Autowired
    private FolderService folderService;


    @PostMapping("/loadFile")
    public ResponseBase loadFile(@RequestBody Files record1) {
        ResponseBase res = new ResponseBase();
        try {

            String accessToken = record1.getAcsTkn();
            int userId = accessService.getAuthenticatedId(accessToken);
            List<Files> records = fileService.getFile();
            User userInfo = userMapper.findByUserId(userId);
            int FolderType =folderController.judgeFolder(Integer.parseInt(String.valueOf(record1.getDirId())));

            for (Files record : records) {

                User fileUser=userMapper.findByUserId(record.getUserId());
                record.setUserName(fileUser.getUsername());
                res.pushData(record);
            }

            res.pushData(FolderType);
            res.pushData(userInfo.getUserId());
            res.pushData(userInfo.getDepartment());
        }
        catch (Exception e) {
            res.setStatus(-1);
            res.setMessage(e.getMessage());
        }
        return res;
    }

    @PostMapping("/moveFile")
    public ResponseEntity<ResponseBase> moveFile(@RequestBody Files record) {
        ResponseBase response = new ResponseBase();
        String accessToken = record.getAcsTkn();
        int userId = accessService.getAuthenticatedId(accessToken);
        User userInfo = userMapper.findByUserId(userId);
        int res_code = fileService.moveFile(userInfo,record);
        if (res_code==0) {
            response.setStatus(-1);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/delFile")
    public ResponseEntity<ResponseBase> delFile(@RequestBody Files record) {
        ResponseBase response = new ResponseBase();
        String accessToken = record.getAcsTkn();
        int userId = accessService.getAuthenticatedId(accessToken);
        User userInfo = userMapper.findByUserId(userId);

        int res_code = fileService.delFile(userInfo,record);
        if (res_code==0) {
            response.setStatus(-1);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/remarkFile")
    public ResponseEntity<ResponseBase> remarkFile(@RequestBody Files record) {
        ResponseBase response = new ResponseBase();
        String accessToken = record.getAcsTkn();
        int userId = accessService.getAuthenticatedId(accessToken);
        User userInfo = userMapper.findByUserId(userId);
        int res_code = fileService.remarkFile(userInfo,record);
        if (res_code==0) {
            response.setStatus(-1);
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/modifyFile")
    public ResponseEntity<ResponseBase> modifyFile(@RequestBody Files record) {
        ResponseBase response = new ResponseBase();
        String accessToken = record.getAcsTkn();
        int userId = accessService.getAuthenticatedId(accessToken);
        User userInfo = userMapper.findByUserId(userId);
        int res_code = fileService.modifyFile(userInfo,record);
        if (res_code==0) {
            response.setStatus(-1);
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PostMapping("/judgeFileType")
    public ResponseEntity<ResponseBase> judgeFileType(@RequestBody Files record) {
        ResponseBase response = new ResponseBase();
        int res_code = folderService.judgeFolder(record.getId());
        response.pushData(res_code);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<ResponseBase> uploadFile(@RequestBody Files record) {
        ResponseBase response = new ResponseBase();
        int res_code = fileService.uploadFile(null,record);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }//写错的


    @PostMapping("/upload")
    public ResponseEntity<ResponseBase> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("folder_id") String folderId,  // 接收 folder_id
            @RequestParam("accessToken") String accessToken,
            @RequestParam("fileName") String fileName
            ) {  // 获取 accessToken) {        // 接收 user

        ResponseBase response = new ResponseBase();
        int user_id = accessService.getAuthenticatedId(accessToken);
        // 定义文件保存路径
        User userInfo = userMapper.findByUserId(user_id);
        String uploadDir = System.getProperty("user.dir") + "/uploads/";  // 相对于项目根目录的路径
        File uploadDirectory = new File(uploadDir);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs(); // 确保上传目录存在
        }

        String allFileName = file.getOriginalFilename();
        String filePath = uploadDir + allFileName;
        try {
            // 保存文件到服务器

            File targetFile = new File(filePath);
            file.transferTo(targetFile);
            // 生成文件的 URL
//            String fileUrl = "http://localhost:8080/" + allFileName;  // 根据你实际的 URL 配置修改
//            System.out.println(fileUrl);
//            response.pushData(fileUrl);
            String fileUrl = "http://localhost:8080/" + allFileName;  // 相对路径的 URL
            response.pushData(fileUrl);

            Files record = new Files();

            record.setFilePath(filePath);
            int dotIndex = allFileName.lastIndexOf('.');
            int dotIndex1 = fileName.lastIndexOf('.');
            // 分离文件名和扩展名
            if (Objects.isNull(fileName) || fileName.isEmpty()) {
                fileName = allFileName.substring(0, dotIndex);
            }else{
                fileName = fileName.substring(0,dotIndex1);
            }
            //区分web和wechat

            String ext = allFileName.substring(dotIndex + 1);
            record.setUrl(fileUrl);
            record.setUserId(user_id);
            record.setDirId(Integer.valueOf(folderId));
            record.setExt(ext);
            record.setFileName(fileName);
            record.setSize(String.valueOf(file.getSize()));
            record.setDepartment(userInfo.getDepartment());

            int res_code=0;
            res_code = fileService.uploadFile(userInfo,record);
            if(res_code==0) {
                response.setStatus(-1);
            }
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IOException e) {
            e.printStackTrace();  // 打印详细错误信息
            response.setMessage("上传失败");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/getFileStatistics")
    public ResponseBase getFileStatistics() {
        HashMap<String, Integer> statistics = new HashMap<>();
        ResponseBase res = new ResponseBase();
        List<Files> records = fileService.getFile();
        int count=0;
        for (Files record : records) {
            String fileExtension = record.getExt();
            // 如果map中已经存在该扩展名的记录，数量加1，否则初始化为1
            statistics.put(fileExtension, statistics.getOrDefault(fileExtension, 0) + 1);
            count++;
        }
        res.pushData(statistics);
        res.pushData(count);
        return res;
    }

//        Files files = loadFile();
//
//        statistics.put("自己布置未完成", my_service.getUnfinSelf());
//        statistics.put("自己布置已完成", my_service.getFinishedSelf());
//        statistics.put("上级布置已完成", my_service.getFinishedMana());
//        statistics.put("上级布置未完成", my_service.getUnfinMana());
//        res.pushData(my_service.getTotal());
//        res.pushData(statistics);

}





