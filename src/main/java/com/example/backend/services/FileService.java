package com.example.backend.services;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.controllers.FolderController;
import com.example.backend.entity.Files;
import com.example.backend.entity.User;
import com.example.backend.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class FileService extends ServiceImpl<FileMapper, Files> {

    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private FolderController folderController;

    public List<Files> getFileByUserId(int userId) {
        return  fileMapper.getFileByUserId(userId);
    }


    public int moveFile(User userinfo, Files record) {
        int res_code = 0;
        int FileTypeFrom=folderController.judgeFolder(record.getBeforeDirId());
        int FileTypeTo=folderController.judgeFolder(record.getDirId());
        if(FileTypeFrom==-2&&FileTypeTo==-2){
            if(Objects.equals(userinfo.getRole(), "admin")) {
                for (int id : record.getIds()) {
                    res_code = fileMapper.moveFile(id, record.getDirId(),userinfo.getUserId(),null);
                }
            }
            return res_code;
        }
        if(FileTypeFrom==-2&&FileTypeTo==-1){
            if(Objects.equals(userinfo.getRole(), "admin")) {
                for (int id : record.getIds()) {
                    res_code = fileMapper.moveFile(id, record.getDirId(),userinfo.getUserId(),userinfo.getDepartment());
                }
            }
            return res_code;
        }
        if(FileTypeFrom==-2&&FileTypeTo==0){
            if(Objects.equals(userinfo.getRole(), "admin")) {
                for (int id : record.getIds()) {
                    res_code = fileMapper.moveFile(id, record.getDirId(),userinfo.getUserId(),null);
                }
            }
            return res_code;
        }
        if(FileTypeFrom==0&&FileTypeTo==-2){
            if(Objects.equals(userinfo.getRole(), "admin")) {
                for (int id : record.getIds()) {
                    res_code = fileMapper.moveFile(id, record.getDirId(),userinfo.getUserId(),null);
                }
            }
            return res_code;
        }
        if(FileTypeFrom==-1&&FileTypeTo==-2){
            if(Objects.equals(userinfo.getRole(), "admin")) {
                for (int id : record.getIds()) {
                    res_code = fileMapper.moveFile(id, record.getDirId(),userinfo.getUserId(),null);
                }
            }
            return res_code;
        }
        if(FileTypeFrom==0&&FileTypeTo==-1){
            for (int id : record.getIds()) {
                res_code = fileMapper.moveFile(id, record.getDirId(),userinfo.getUserId(), userinfo.getDepartment());
            }
            return res_code;
        }
        if(FileTypeFrom==0&&FileTypeTo==0){
            for (int id : record.getIds()) {
                res_code = fileMapper.moveFile(id, record.getDirId(),userinfo.getUserId(), null);
            }
            return res_code;
        }
        if(FileTypeFrom==-1&&FileTypeTo==0){
            for (int id : record.getIds()) {
                res_code = fileMapper.moveFile(id, record.getDirId(),userinfo.getUserId(), null);
            }
            return res_code;
        }
        if(FileTypeFrom==-1&&FileTypeTo==-1){
            for (int id : record.getIds()) {
                res_code = fileMapper.moveFile(id, record.getDirId(),userinfo.getUserId(), userinfo.getDepartment());
            }
            return res_code;
        }
        return res_code;
    }

    public int delFolder(User userinfo,Files record) {
        int res_code = 0;
        int FileType=folderController.judgeFolder(record.getBeforeDirId());
        if(FileType==-2){
            if(Objects.equals(userinfo.getRole(), "admin")) {
                for (int id : record.getIds()) {
                    res_code = fileMapper.delFolder(id);
                }
            }
            return res_code;
        }
        for (int id : record.getIds()) {
            res_code = fileMapper.delFolder(id);
        }
        return res_code;
    }

    public int remarkFile(User userinfo,Files record) {
        System.out.println(record.getIds());
        int res_code = 0;
        int FileType=folderController.judgeFolder(record.getBeforeDirId());
        if(FileType==-2){
            if(Objects.equals(userinfo.getRole(), "admin")) {
                for (int id : record.getIds()) {
                    res_code = fileMapper.remarkFile(id,record.getRemark(),userinfo.getUserId());
                }
            }
            return res_code;
        }
        for (int id : record.getIds()) {
            res_code = fileMapper.remarkFile(id,record.getRemark(),userinfo.getUserId());
        }
        return res_code;
    }


    public int uploadFile(String department,Files record) {
        System.out.println(record.getFileName());
        int res_code = fileMapper.uploadFile(record.getFileName(),record.getExt(),
                    record.getSize(),record.getDirId(), String.valueOf(record.getUserId()),record.getUrl(),department);

        return res_code;
    }

    public List<Files> getFile() {
        return  fileMapper.getFile();
    }
}
