package com.example.AttendanceManage.model;

//import lombok.Data;
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
    private String location;
    private LocalTime startTime;
    private LocalTime endTime;
    private int breakDuration;


}
