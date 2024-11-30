package com.example.backend.entity.meeting;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class MeeetingWithTk {
    private int mtin_id;
    private int adder_id;
    private String mtin_title;
    private String mtin_ctnt;
    private String mtin_fin;
    private String mtin_st;
    private String mtin_len;
    private String mtin_host;
    private String mtin_loc;
    private String mtin_crt;
    private String acsTkn;

    public MeeetingWithTk(int mtin_id, int adder_id, String mtin_title, String mtin_ctnt, String mtin_fin,
                          String mtin_st, String mtin_len, String mtin_host, String mtin_loc, String mtin_crt, String acsTkn) {
        this.mtin_id = mtin_id;
        this.adder_id = adder_id;
        this.mtin_title = mtin_title;
        this.mtin_ctnt = mtin_ctnt;
        this.mtin_fin = mtin_fin;
        this.mtin_st = mtin_st;
        this.mtin_len = mtin_len;
        this.mtin_host = mtin_host;
        this.mtin_loc = mtin_loc;
        this.mtin_crt = mtin_crt;
        this.acsTkn = acsTkn;
    }
}
