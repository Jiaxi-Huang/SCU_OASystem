package com.example.backend.entity.leave;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("leave_requests")
public class LeaveJoinNotifyRecord {

    private int leave_id;
    private int user_id;
    private int review_user_id;
    private String start_date;
    private String end_date;
    private String type;
    private String reason;
    private String status;
    private String submitted_at;
    private int notification_id;
    private int cc_user_id;
    private int notified_user_id;
    private int request_id;
    private String request_type;
    private String notified_at;

    public LeaveJoinNotifyRecord(int leave_id, int user_id, int review_user_id, String start_date, String end_date, String type, String reason, String status, String submitted_at, int notification_id, int cc_user_id, int notified_user_id, int request_id, String request_type, String notified_at) {
        this.leave_id = leave_id;
        this.user_id = user_id;
        this.review_user_id = review_user_id;
        this.start_date = start_date;
        this.end_date = end_date;
        this.type = type;
        this.reason = reason;
        this.status = status;
        this.submitted_at = submitted_at;
        this.notification_id = notification_id;
        this.cc_user_id = cc_user_id;
        this.notified_user_id = notified_user_id;
        this.request_id = request_id;
        this.request_type = request_type;
        this.notified_at = notified_at;
    }
}
