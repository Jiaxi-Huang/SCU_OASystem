package com.example.backend.entity.meeting;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@TableName("meetings")
public class Meeting {
    private int mtin_id;
    private String mtin_title;
    private String mtin_ctnt;
    private String mtin_fin;
    private String mtin_st;
    private String mtin_len;
    private String mtin_host;
    private String mtin_loc;
    private String mtin_crt;

    public Meeting(int mtin_id, String mtin_title, String mtin_ctnt, String mtin_st,
                   String mtin_fin, String mtin_len, String mtin_host, String mtin_loc, String mtin_crt) {
        this.mtin_id = mtin_id;
        this.mtin_title = mtin_title;
        this.mtin_ctnt = mtin_ctnt;
        this.mtin_st = mtin_st;
        this.mtin_fin = mtin_fin;
        this.mtin_len = mtin_len;
        this.mtin_host = mtin_host;
        this.mtin_loc = mtin_loc;
        this.mtin_crt = mtin_crt;
    }

}
