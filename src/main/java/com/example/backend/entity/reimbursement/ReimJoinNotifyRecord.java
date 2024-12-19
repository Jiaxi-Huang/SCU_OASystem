package com.example.backend.entity.reimbursement;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter

public class ReimJoinNotifyRecord {
    private int reimbursement_id;
    private int user_id;
    private BigDecimal amount;
    private String description;
    private String status;
    private String submitted_at;
    private int notification_id;
    private int notified_user_id;
    private String request_type;
    private int request_id;
    private String notified_at;
    private int cc_user_id;

    public ReimJoinNotifyRecord(int reimbursement_id, int user_id, BigDecimal amount, String description, String status, String submitted_at, int notification_id, int notified_user_id, String request_type, int request_id, String notified_at, int cc_user_id) {
        this.reimbursement_id = reimbursement_id;
        this.user_id = user_id;
        this.amount = amount;
        this.description = description;
        this.status = status;
        this.submitted_at = submitted_at;
        this.notification_id = notification_id;
        this.notified_user_id = notified_user_id;
        this.request_type = request_type;
        this.request_id = request_id;
        this.notified_at = notified_at;
        this.cc_user_id = cc_user_id;
    }
}
