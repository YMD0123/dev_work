package com.example.AttendanceManage.model;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class Attendance {
    private int id;
    private int userId;
    private String departmentCode;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private int breakDuration;



    public void setId(Integer id) {
    }

    public void setUserId(Integer userId) {
    }

    public void setDepartmentCode(String departmentCode) {
    }

    public void setDate(LocalDate date) {
    }

    public void setStartTime(LocalTime startTime) {
    }

    public void setEndTime(LocalTime endTime) {
    }

    public void setBreakDuration(Integer breakDuration) {
    }
}
