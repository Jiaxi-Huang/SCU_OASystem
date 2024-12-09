package com.example.backend.controllers;

import com.example.backend.entity.Folder;
import com.example.backend.entity.ResponseBase;
import com.example.backend.entity.User;
import com.example.backend.entity.userInfo.adminUserInfoRequest;
import com.example.backend.mapper.UserMapper;
import com.example.backend.services.AccessService;
import com.example.backend.services.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/folder")
public class FolderController {

    @Autowired
    private FolderService folderService;

    @Autowired
    private AccessService accessService;

    @Autowired
    UserMapper userMapper;

    public int judgeFolder(int i) {
        int folderType = folderService.judgeFolder(i);
        return folderType;
        //确定folder是在个人，部门还是公司的
        //0是个人，-1是部门，-2是公司
    }

    @PostMapping("/loadFolder")
    public ResponseBase loadFolder(@RequestBody adminUserInfoRequest request) {
        System.out.println("[loadFolder] receive");
        ResponseBase res = new ResponseBase();
        try {
            String accessToken = request.getAccessToken();
            int userId = accessService.getAuthenticatedId(accessToken);
            List<Folder> records = folderService.getFolder();
            User userInfo = userMapper.findByUserId(userId);
            for (Folder record : records) {
                res.pushData(record);
            }
            res.pushData(userInfo.getUserId());
            res.pushData(userInfo.getDepartment());
        }
        catch (Exception e) {
            res.setStatus(-1);
            res.setMessage(e.getMessage());
        }
        return res;
    }

    @PostMapping("/createFolder")
    public ResponseEntity<ResponseBase> createFolder(@RequestBody Folder record) {
        ResponseBase response = new ResponseBase();
        String accessToken = record.getAcsTkn();
        int userId = accessService.getAuthenticatedId(accessToken);
        User userInfo = userMapper.findByUserId(userId);

        int res_code = folderService.createFolder(userInfo,record);
        if(res_code==0){
            response.setStatus(-1);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PostMapping("/modifyFolder")
    public ResponseEntity<ResponseBase> modifyFolder(@RequestBody Folder record) {
        ResponseBase response = new ResponseBase();
        System.out.println(record.getTitle());
        String accessToken = record.getAcsTkn();
        int userId = accessService.getAuthenticatedId(accessToken);
        User userInfo = userMapper.findByUserId(userId);
        int res_code = folderService.modifyFolder(userInfo,record);
        if(res_code==0){
            response.setStatus(-1);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PostMapping("/moveFolder")
    public ResponseEntity<ResponseBase> moveFolder(@RequestBody Folder record) {
        ResponseBase response = new ResponseBase();
        System.out.println(record.getId());
        String accessToken = record.getAcsTkn();
        int userId = accessService.getAuthenticatedId(accessToken);
        System.out.println("moveFoleder"+userId);
        User userInfo = userMapper.findByUserId(userId);
        int res_code = folderService.moveFolder(userInfo,record);
        if(res_code==0){
            response.setStatus(-1);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PostMapping("/delFolder")
    public ResponseEntity<ResponseBase> delFolder(@RequestBody Folder record) {
        ResponseBase response = new ResponseBase();
        System.out.println(record.getId());
        String accessToken = record.getAcsTkn();
        int userId = accessService.getAuthenticatedId(accessToken);
        System.out.println("moveFoleder"+userId);
        User userInfo = userMapper.findByUserId(userId);
        int res_code = folderService.delFolder(userInfo,record);
        if(res_code==0){
            response.setStatus(-1);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
