package com.example.backend.entity.userInfo;

public class userInfoResponse {
    private int status;
    private String message;
    private boolean success;
    private Data data;

    public userInfoResponse(int status, String message, boolean success, Data data) {
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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    // 内部类 Data
    public static class Data {
        private String userName;
        private String roleName;
        private String userDepartment;
        private String userIntro;
        private String userPhone;
        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }

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

        public String getUserIntro() {
            return userIntro;
        }

        public void setUserIntro(String userIntro) {
            this.userIntro = userIntro;
        }

        public Data() {
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }
    }
}
