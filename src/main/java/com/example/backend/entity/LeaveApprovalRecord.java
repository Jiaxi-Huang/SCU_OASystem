package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("leave_requests")
public class LeaveApprovalRecord {

    private int leave_id;  
    private int user_id;  
    private String start_date;  
    private String end_date;  
    private String reason; 
    private String status; 
    private String submitted_at;

    public LeaveApprovalRecord(int leave_id,int user_id, String start_date, String end_date, String reason,
                               String status, String submitted_at) {
        this.leave_id = leave_id;
        this.user_id = user_id;
        this.start_date = start_date;
        this.end_date = end_date;
        this.reason = reason;
        this.status = status;
        this.submitted_at = submitted_at;
    }
}
