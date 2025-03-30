package com.example.backend.services;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
    private FolderService folderService;

    public List<Files> getFileByUserId(int userId) {
        return  fileMapper.getFileByUserId(userId);
    }

    public int moveFile(User userinfo, Files record) {
        int res_code = 0;
        int FileTypeFrom=folderService.judgeFolder(record.getBeforeDirId());
        int FileTypeTo=folderService.judgeFolder(record.getDirId());
        if(FileTypeFrom==-2&&FileTypeTo==-2){
            if(Objects.equals(userinfo.getRole(), "admin")) {
                for (int id : record.getIds()) {
                    res_code = fileMapper.moveFile(id, record.getDirId(),userinfo.getUserId(),null,1);
                }
            }
            return res_code;
        }
        if(FileTypeFrom==-2&&FileTypeTo==-1){
            if(Objects.equals(userinfo.getRole(), "admin")) {
                for (int id : record.getIds()) {
                    res_code = fileMapper.moveFile(id, record.getDirId(),userinfo.getUserId(),userinfo.getDepartment(),0);
                }
            }
            return res_code;
        }
        if(FileTypeFrom==-2&&FileTypeTo==0){
            if(Objects.equals(userinfo.getRole(), "admin")) {
                for (int id : record.getIds()) {
                    res_code = fileMapper.moveFile(id, record.getDirId(),userinfo.getUserId(),null,0);
                }
            }
            return res_code;
        }
        if(FileTypeFrom==0&&FileTypeTo==-2){
            if(Objects.equals(userinfo.getRole(), "admin")) {
                for (int id : record.getIds()) {
                    res_code = fileMapper.moveFile(id, record.getDirId(),userinfo.getUserId(),null,1);
                }
            }
            return res_code;
        }
        if(FileTypeFrom==-1&&FileTypeTo==-2){
            if(Objects.equals(userinfo.getRole(), "admin")) {
                for (int id : record.getIds()) {
                    res_code = fileMapper.moveFile(id, record.getDirId(),userinfo.getUserId(),null,1);
                }
            }
            return res_code;
        }
        if(FileTypeFrom==0&&FileTypeTo==-1){
            for (int id : record.getIds()) {
                res_code = fileMapper.moveFile(id, record.getDirId(),userinfo.getUserId(), userinfo.getDepartment(),0);
            }
            return res_code;
        }
        if(FileTypeFrom==0&&FileTypeTo==0){
            for (int id : record.getIds()) {
                res_code = fileMapper.moveFile(id, record.getDirId(),userinfo.getUserId(), null,0);
            }
            return res_code;
        }
        if(FileTypeFrom==-1&&FileTypeTo==0){
            for (int id : record.getIds()) {
                res_code = fileMapper.moveFile(id, record.getDirId(),userinfo.getUserId(), null,0);
            }
            return res_code;
        }
        if(FileTypeFrom==-1&&FileTypeTo==-1){
            for (int id : record.getIds()) {
                res_code = fileMapper.moveFile(id, record.getDirId(),userinfo.getUserId(), userinfo.getDepartment(),0);
            }
            return res_code;
        }
        return res_code;
    }

    public int delFile(User userinfo,Files record) {
        int res_code = 0;
        int FileType=folderService.judgeFolder(record.getBeforeDirId());
        if(FileType==-2){
            if(Objects.equals(userinfo.getRole(), "admin")) {
                for (int id : record.getIds()) {
                    res_code = fileMapper.delFile(id);
                }
            }
            return res_code;
        }
        for (int id : record.getIds()) {
            res_code = fileMapper.delFile(id);
        }
        return res_code;
    }

    public int remarkFile(User userinfo,Files record) {
        int res_code = 0;
        int FileType=folderService.judgeFolder(record.getBeforeDirId());
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


    public int uploadFile(User userinfo,Files record) {
        int fileType = folderService.judgeFolder(record.getDirId());
        int res_code=0;
        if(fileType==-2){
            if(Objects.equals(userinfo.getRole(), "admin")) {
                res_code = fileMapper.uploadFile(record.getFileName(), record.getExt(),
                        record.getSize(), record.getDirId(),
                        String.valueOf(record.getUserId()), record.getUrl(), null,1);
            }
            return res_code;
        }
        if(fileType==-1){
            res_code = fileMapper.uploadFile(record.getFileName(), record.getExt(),
                    record.getSize(), record.getDirId(),
                    String.valueOf(record.getUserId()), record.getUrl(), record.getDepartment(),0);
        }
        if(fileType==0){
            res_code = fileMapper.uploadFile(record.getFileName(), record.getExt(),
                    record.getSize(), record.getDirId(),
                    String.valueOf(record.getUserId()), record.getUrl(), null,0);
        }
        return res_code;
    }

    public List<Files> getFile() {
        return  fileMapper.getFile();
    }

    public int modifyFile(User userInfo, Files record) {
        int fileType = folderService.judgeFolder(record.getDirId());
        int res_code=0;
        if(fileType==-2){
            if(Objects.equals(userInfo.getRole(), "admin")) {
                res_code = fileMapper.modifyFile(record.getId(), record.getFileName(),
                        userInfo.getUserId());
            }
            return res_code;
        }
        if(fileType==-1||fileType==0){
            res_code = fileMapper.modifyFile(record.getId(), record.getFileName(),
                    userInfo.getUserId());
        }
        return  res_code;
    }

    public void updateFileDepartmentAndShared(Integer id, String department, int isShared, int userId) {
        int res_code = fileMapper.updateFolderDepartmentAndShared(id,department,isShared,userId);
    }
}
