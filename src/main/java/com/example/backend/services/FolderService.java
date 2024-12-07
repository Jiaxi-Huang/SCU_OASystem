package com.example.backend.services;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.entity.Folder;
import com.example.backend.entity.User;
import com.example.backend.mapper.FolderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class FolderService extends ServiceImpl<FolderMapper, Folder> {

    @Autowired
    private FolderMapper folderMapper;
    public List<Folder> getFolderByUserId(int userId) {
        return  folderMapper.getFolderByUserId(userId);
    }

    public int createFolder(User userinfo, Folder record) {
        System.out.println(record.getId());
        int FolderType=judgeFolder(record.getPid());
        int res_code=0;
        if(FolderType==0){
            res_code = folderMapper.createFolder(userinfo.getUserId(),
                    null,record.getTitle(), record.getPid());
        }
        if(FolderType==-1){
            res_code = folderMapper.createFolder(userinfo.getUserId(),
                    userinfo.getDepartment(),record.getTitle(), record.getPid());
        }
        if(FolderType==-2){
            if(Objects.equals(userinfo.getRole(), "admin")){
                res_code = folderMapper.createFolder(userinfo.getUserId(),
                        null,record.getTitle(), record.getPid());
            }

        }
        System.out.println("res code"+res_code);
        return res_code;
    }

    public int modifyFolder(User userinfo, Folder record) {
        System.out.println(record.getId());
        int FolderType=judgeFolder(record.getId());
        int res_code=0;
        if(FolderType==0||FolderType==-1){
            res_code = folderMapper.modifyFolder(record.getId(), record.getTitle());
        }
        if(FolderType==-2){
            if(Objects.equals(userinfo.getRole(), "admin")){
                res_code = folderMapper.modifyFolder(record.getId(), record.getTitle());
            }

        }
        System.out.println("res code"+res_code);
        return res_code;

    }

    public int moveFolder(User userinfo, Folder record) {
        System.out.println(record.getId());
        int FolderTypeFrom=judgeFolder(record.getId());
        int FolderTypeTo=judgeFolder(record.getPid());
        String department = userinfo.getDepartment();
        System.out.println("department"+department);
        int res_code=0;
        if(FolderTypeTo==0){
            if(FolderTypeFrom==-1||FolderTypeFrom==0){
                res_code = folderMapper.moveFolder(record.getId(), record.getPid(), null,userinfo.getUserId());
            }
            if(FolderTypeFrom==-2){
                if(Objects.equals(userinfo.getRole(), "admin")) {
                    res_code = folderMapper.moveFolder(record.getId(), record.getPid(), null,userinfo.getUserId());
                }
            }
            return res_code;
        }
        if(FolderTypeTo==-1){
            if(FolderTypeFrom==-1||FolderTypeFrom==0){
                res_code = folderMapper.moveFolder(record.getId(), record.getPid(), department,userinfo.getUserId());
            }
            if(FolderTypeFrom==-2){
                if(Objects.equals(userinfo.getRole(), "admin")) {
                    res_code = folderMapper.moveFolder(record.getId(),record.getPid(),department,userinfo.getUserId());
                }
            }
            return res_code;
        }
        if(FolderTypeTo==-2){
            if(Objects.equals(userinfo.getRole(), "admin")) {
                res_code = folderMapper.moveFolder(record.getId(), record.getPid(), null,userinfo.getUserId());
            }
            return res_code;
        }
        return res_code;
    }


    public int delFolder(User userinfo,Folder record) {
        System.out.println(record.getId());
        int res_code=0;
        int FolderType=judgeFolder(record.getId());
        if(FolderType==-2){
            if(Objects.equals(userinfo.getRole(), "admin")) {
                res_code = folderMapper.delFolder(record.getId());
            }
            return res_code;
        }
        res_code = folderMapper.delFolder(record.getId());
        return res_code;
    }

    public int judgeFolder(int id) {
        if(id==0||id==-1||id==-2){
            return id;
        }
        Folder folder = folderMapper.judgeFolder(id);
        if(folder==null){
            return -3;
        }
        while(folder.getPid()!=0&&folder.getPid()!=-1&&folder.getPid()!=-2){
            id=folder.getPid();
            folder = folderMapper.judgeFolder(id);
            if(folder==null){
                return -3;
            }
        }
        return folder.getPid();
    }

    public List<Folder> getFolder() {
        return  folderMapper.getFolder();
    }
}
