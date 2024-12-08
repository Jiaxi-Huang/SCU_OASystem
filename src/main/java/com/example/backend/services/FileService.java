package com.example.backend.services;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.entity.Files;
import com.example.backend.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService extends ServiceImpl<FileMapper, Files> {

    @Autowired
    private FileMapper fileMapper;
    public List<Files> getFileByUserId(int userId) {
        return  fileMapper.getFileByUserId(userId);
    }


    public int moveFile(Files record) {
        int res_code = 0;
        for (int id : record.getIds()) {
            res_code = fileMapper.moveFile(id, record.getDirId());
        }
        return res_code;
    }

    public int delFolder(Files record) {
        int res_code = 0;
        for (int id : record.getIds()) {
            res_code = fileMapper.delFolder(id);
        }
        return res_code;
    }

    public int remarkFile(Files record) {
        System.out.println(record.getIds());
        int res_code = 0;
        for (int id : record.getIds()) {
            res_code = fileMapper.remarkFile(id,record.getRemark());
        }
        return res_code;
    }


    public int uploadFile(Files record) {
        System.out.println(record.getFileName());
        int res_code = fileMapper.uploadFile(record.getFileName(),record.getExt(),
                    record.getSize(),record.getDirId(), String.valueOf(record.getUserId()),record.getUrl());

        return res_code;
    }

    public List<Files> getFile() {
        return  fileMapper.getFile();
    }
}
