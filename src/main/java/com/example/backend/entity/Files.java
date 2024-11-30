package com.example.backend.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class Files {
    // Getters and Setters
    private List<Integer> ids;
    private Integer id;
    private String fileName;
    private String ext;
    private String size;
    private Integer dirId; // 指向文件夹的ID
    private String url;
    private String remark;
    private Date createTime;
    private Date updateTime;
    private String userId;
    private String filePath;

}
