package com.example.backend.entity.userInfo;

public class personalSettingResponse {
    private int status;
    private String message;
    private boolean success;
    private Data data;

    public personalSettingResponse(int status, String message, boolean success, Data data) {
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
        private String username;
        private String userphone;
        private String userintro;

        public String getUserName() {
            return username;
        }

        public void setUserName(String username) {
            this.username = username;
        }

        public String getUserPhone() {
            return userphone;
        }

        public void setUserPhone(String userphone) {
            this.userphone = userphone;
        }

        public String getUserIntro() {
            return userintro;
        }

        public void setUserIntro(String userintro) {
            this.userintro = userintro;
        }

        public Data(String username, String phone,String intro) {
            setUserName(username);
            setUserPhone(phone);
            setUserIntro(intro);
        }
    }
}
