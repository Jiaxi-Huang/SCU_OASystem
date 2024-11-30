package com.example.backend.controllers;

import com.example.backend.entity.Folder;
import com.example.backend.entity.ResponseBase;
import com.example.backend.entity.userInfo.adminUserInfoRequest;
import com.example.backend.services.AccessService;
import com.example.backend.services.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/folder")
public class FolderController {

    @Autowired
    private FolderService folderService;

    @Autowired
    private AccessService accessService;


    @PostMapping("/loadFolder")
    public ResponseBase loadFolder(@RequestBody adminUserInfoRequest request) {
        System.out.println("[loadFolder] receive");
        ResponseBase res = new ResponseBase();
        try {
            String accessToken = request.getAccessToken();
            int userId = accessService.getAuthenticatedId(accessToken);
            List<Folder> records = folderService.getFolderByUserId(userId);
            for (Folder record : records) {
                res.pushData(record);
            }
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
        int res_code = folderService.createFolder(userId,record);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PostMapping("/modifyFolder")
    public ResponseEntity<ResponseBase> modifyFolder(@RequestBody Folder record) {
        ResponseBase response = new ResponseBase();
        System.out.println(record.getTitle());

        int res_code = folderService.modifyFolder(record);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PostMapping("/moveFolder")
    public ResponseEntity<ResponseBase> moveFolder(@RequestBody Folder record) {
        ResponseBase response = new ResponseBase();
        System.out.println(record.getId());

        int res_code = folderService.moveFolder(record);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PostMapping("/delFolder")
    public ResponseEntity<ResponseBase> delFolder(@RequestBody Folder record) {
        ResponseBase response = new ResponseBase();
        System.out.println(record.getId());

        int res_code = folderService.delFolder(record);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
