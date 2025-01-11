package com.example.backend.entity.userInfo;

import java.util.List;
public class userStatisticResponse {
    private int status;
    private String message;
    private boolean success;
    private Integer onlineUsers;
    private List<Data> data;

    public userStatisticResponse(int status, String message, boolean success, Integer onlineUsers, List<Data> data) {
        this.status = status;
        this.message = message;
        this.success = success;
        this.onlineUsers = onlineUsers;
        this.data = data;
    }

    public static class Data {
        private String name;
        private int value;
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getOnlineUsers() {
        return onlineUsers;
    }

    public void setOnlineUsers(Integer onlineUsers) {
        this.onlineUsers = onlineUsers;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }
}
