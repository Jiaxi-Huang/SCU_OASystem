package com.example.backend.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotifyRecord {
    private int notification_id;
    private int notified_user_id;
    private String request_type;
    private int request_id;
    private String notified_at;
    private int user_id;
    private String accessToken;

    public NotifyRecord(int notification_id, int notified_user_id, String request_type, int request_id, String notified_at, int user_id, String accessToken) {
        this.notification_id = notification_id;
        this.notified_user_id = notified_user_id;
        this.request_type = request_type;
        this.request_id = request_id;
        this.notified_at = notified_at;
        this.user_id = user_id;
        this.accessToken = accessToken;
    }
}
