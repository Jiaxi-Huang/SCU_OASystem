package com.example.backend.entity.leave;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("leave_requests")
public class LeaveApprovalRecordWithAccessToken {

    private int leave_id;
    private int user_id;
    private int review_user_id;
    private String start_date;
    private String end_date;
    private String type;
    private String reason;
    private String status;
    private String submitted_at;
    private String accessToken;
    private int[] cc_user; // 抄送人数组

    public LeaveApprovalRecordWithAccessToken(int leave_id, int user_id, int review_user_id, String start_date, String end_date, String type, String reason, String status, String submitted_at, String accessToken, int[] cc_user) {
        this.leave_id = leave_id;
        this.user_id = user_id;
        this.review_user_id = review_user_id;
        this.start_date = start_date;
        this.end_date = end_date;
        this.type = type;
        this.reason = reason;
        this.status = status;
        this.submitted_at = submitted_at;
        this.accessToken = accessToken;
        this.cc_user = cc_user;
    }
}
