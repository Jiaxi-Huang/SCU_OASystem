package com.example.backend.controllers;

import com.example.backend.entity.Files;
import com.example.backend.entity.Folder;
import com.example.backend.entity.ResponseBase;
import com.example.backend.entity.User;
import com.example.backend.entity.userInfo.adminUserInfoRequest;
import com.example.backend.mapper.UserMapper;
import com.example.backend.services.AccessService;
import com.example.backend.services.FileService;
import com.example.backend.services.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/folder")
public class FolderController {

    @Autowired
    private FolderService folderService;

    @Autowired
    private AccessService accessService;

    @Autowired
    UserMapper userMapper;
    @Autowired
    private FileService fileService;

    public int judgeFolder(int i) {
        int folderType = folderService.judgeFolder(i);
        return folderType;
        //确定folder是在个人，部门还是公司的
        //0是个人，-1是部门，-2是公司
    }


    public void folderBFS(String department, int isShared, int folderId,int userId) {
        // 获取所有文件夹和文件数据
        List<Folder> folderList = folderService.getFolder();
        List<Files> filesList = fileService.getFile();

        // 使用队列进行广度优先搜索
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(folderId);

        // 存储文件夹ID和文件夹映射，便于快速查找子文件夹
        Map<Integer, List<Folder>> folderMap = new HashMap<>();
        for (Folder folder : folderList) {
            folderMap.computeIfAbsent(folder.getPid(), k -> new ArrayList<>()).add(folder);
        }

        // 存储文件夹ID和文件映射，便于快速查找文件
        Map<Integer, List<Files>> fileMap = new HashMap<>();
        for (Files file : filesList) {
            fileMap.computeIfAbsent(file.getDirId(), k -> new ArrayList<>()).add(file);
        }

        // 广度优先搜索更新子文件夹和文件
        while (!queue.isEmpty()) {
            int currentFolderId = queue.poll();

            // 更新当前文件夹
            folderService.updateFolderDepartmentAndShared(currentFolderId, department, isShared,userId);

            // 更新当前文件夹中的文件
            List<Files> filesInCurrentFolder = fileMap.get(currentFolderId);
            if (filesInCurrentFolder != null) {
                for (Files file : filesInCurrentFolder) {
                    fileService.updateFileDepartmentAndShared(file.getId(), department, isShared,userId);
                }
            }

            // 将子文件夹加入队列
            List<Folder> subFolders = folderMap.get(currentFolderId);
            if (subFolders != null) {
                for (Folder subFolder : subFolders) {
                    queue.offer(subFolder.getId());
                }
            }
        }
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
        String[] result = folderService.moveFolder(userInfo,record);
        int res_code = Integer.parseInt(result[0]);
        if(res_code==0){
            response.setStatus(-1);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        folderBFS(result[1], Integer.parseInt(result[2]),record.getId(),userId);
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
        if(record.getId()==0||record.getId()==-1||record.getId()==-2){
            response.setStatus(-2);
        }else{
            int res_code = folderService.delFolder(userInfo,record);
            if(res_code==0){
                response.setStatus(-1);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
