package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("users")
public class User {

    private int id;
    private String email;
    private String password;
    private String phone;
    private String role;

    // Getters and Setters
    public int getId() {
        return id;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String name) {
        this.phone = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

