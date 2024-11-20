package com.example.backend.entity.userInfo;

import java.util.List;

public class adminUserInfoResponse {
    private int status;
    private String message;
    private boolean success;

    private List<Data> data;

    public adminUserInfoResponse(int status, String message, boolean success, List<Data> data) {
        this.status = status;
        this.message = message;
        this.success = success;
        this.data = data;
    }
    // 内部类 Data
    public static class Data {
        private String userName;
        private String userDepartment;
        private String userRole;
        private String userPhone;
                public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserDepartment() {
            return userDepartment;
        }

        public void setUserDepartment(String userDepartment) {
            this.userDepartment = userDepartment;
        }

        public String getUserRole() {
            return userRole;
        }

        public void setUserRole(String userRoleName) {
            this.userRole = userRoleName;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
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
