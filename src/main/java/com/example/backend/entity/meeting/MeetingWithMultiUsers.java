package com.example.backend.entity.meeting;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter

public class MeetingWithMultiUsers {
    private String mtin_title;
    private String mtin_ctnt;
    private int mtin_fin;
    private String mtin_st;
    private String mtin_len;
    private String mtin_loc;
    private String accessToken;
    private ArrayList<Integer> user_ids;

    public MeetingWithMultiUsers(String mtin_title, String mtin_ctnt, int mtin_fin, String mtin_st, String mtin_len,
                                 String mtin_loc, String accessToken, ArrayList<Integer> user_ids) {
        this.mtin_title = mtin_title;
        this.mtin_ctnt = mtin_ctnt;
        this.mtin_fin = mtin_fin;
        this.mtin_st = mtin_st;
        this.mtin_len = mtin_len;
        this.mtin_loc = mtin_loc;
        this.accessToken = accessToken;
        this.user_ids = user_ids;
    }
}
