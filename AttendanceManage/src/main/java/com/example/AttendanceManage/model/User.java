package com.example.AttendanceManage.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class User {
    private int id;
    private String username;
    private String password;
    private String role;
    private String departmentCode;
    private String phoneNumber;
    private String email;
}
