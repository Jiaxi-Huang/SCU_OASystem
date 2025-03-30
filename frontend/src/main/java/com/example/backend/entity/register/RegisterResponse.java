package com.example.backend.entity.register;

public class RegisterResponse {
    private int status;
    private String message;
    private boolean success;
    private Data data;

    public RegisterResponse(int status, String message, boolean success, Data data) {
        this.status = status;
        this.message = message;
        this.success = success;
        this.data = data;
    }

    // Getter 和 Setter 方法
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

    public RegisterResponse.Data getData() {
        return data;
    }

    public void setData(RegisterResponse.Data data) {
        this.data = data;
    }

    // 内部类 Data
    public static class Data{
    }
}
