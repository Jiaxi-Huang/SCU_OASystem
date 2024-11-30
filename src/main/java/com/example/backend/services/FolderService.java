package com.example.backend.services;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.entity.Folder;
import com.example.backend.mapper.FolderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FolderService extends ServiceImpl<FolderMapper, Folder> {

    @Autowired
    private FolderMapper folderMapper;
    public List<Folder> getFolderByUserId(int userId) {
        return  folderMapper.getFolderByUserId(userId);
    }

    public int createFolder(int userId,Folder record) {
        System.out.println(record.getId());
        int res_code = folderMapper.createFolder(userId,record.getTitle(), record.getPid());
        return res_code;
    }

    public int modifyFolder(Folder record) {
        System.out.println(record.getId());
        int res_code = folderMapper.modifyFolder(record.getId(),record.getTitle());
        return res_code;
    }

    public int moveFolder(Folder record) {
        System.out.println(record.getId());
        int res_code = folderMapper.moveFolder(record.getId(),record.getPid());
        return res_code;
    }


    public int delFolder(Folder record) {
        System.out.println(record.getId());
        int res_code = folderMapper.delFolder(record.getId());
        return res_code;
    }
}
