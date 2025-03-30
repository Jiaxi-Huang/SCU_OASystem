package com.example.backend.entity.department;

import java.util.List;

public class adminDepartmentInfoResponse {
    private int status;
    private String message;
    private boolean success;

    private List<Data> data;

    public adminDepartmentInfoResponse(int status, String message, boolean success, List<Data> data) {
        this.status = status;
        this.message = message;
        this.success = success;
        this.data = data;
    }
    // 内部类 Data
    public static class Data {
        private int departmentId;
        private String departmentName;
        public int getDepartmentId() {
            return departmentId;
        }

        public void setDepartmentId(int departmentId) {
            this.departmentId = departmentId;
        }

        public String getDepartmentName() {
            return departmentName;
        }

        public void setDepartmentName(String departmentName) {
            this.departmentName = departmentName;
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

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }
}
