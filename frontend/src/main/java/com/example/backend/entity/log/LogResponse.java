package com.example.backend.entity.log;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LogResponse {
    private int status;
    private String message;
    private boolean success;
    private List<Data> data;
    public LogResponse(int status, String message, boolean success, List<Data> data) {
        this.status = status;
        this.message = message;
        this.success = success;
        this.data = data;
    }

    public static class Data {
        private int id;
        private int userId;
        private String logContent;
        private String logDate;

        public Data() {

        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getLogContent() {
            return logContent;
        }

        public void setLogContent(String logContent) {
            this.logContent = logContent;
        }

        public String getLogDate() {
            return logDate;
        }

        public void setLogDate(String logDate) {
            this.logDate = logDate;
        }

    }
}
