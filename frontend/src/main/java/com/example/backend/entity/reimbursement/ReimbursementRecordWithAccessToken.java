package com.example.backend.entity.reimbursement;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ReimbursementRecordWithAccessToken {
    private int reimbursement_id;
    private int user_id;
    private BigDecimal amount;
    private String description;
    private String status;
    private String submitted_at;
    private int review_user_id;
    private String accessToken;
    private int[] cc_user; // 抄送人数组

    public ReimbursementRecordWithAccessToken(int reimbursement_id, int user_id, BigDecimal amount, String description, String status, String submitted_at, int review_user_id, String accessToken, int[] cc_user) {
        this.reimbursement_id = reimbursement_id;
        this.user_id = user_id;
        this.amount = amount;
        this.description = description;
        this.status = status;
        this.submitted_at = submitted_at;
        this.review_user_id = review_user_id;
        this.accessToken = accessToken;
        this.cc_user = cc_user;
    }
}
