package com.example.AttendanceManage.model;

import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Table(name = "users")
public class User {

    private int id;
    private String username;
    private String password;
    private String role;
    private String departmentCode;
    private String phoneNumber;
    private String email;

    public User(int id, String username, String role, String department_code) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.departmentCode = department_code;
    }

    public User() {

    }


    @Override
    public String toString() {
        return "id:" + this.id + "名前" + this.username + "権限" + this.role + "部署" + this.departmentCode;
    }
}
