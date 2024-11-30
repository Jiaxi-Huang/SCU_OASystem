package com.example.backend.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Folder {
    // Getters and Setters
    private Integer id;
    private String title;
    private Integer pid; // 父文件夹ID
    private List<Folder> children; // 子文件夹
    private String acsTkn;
}
