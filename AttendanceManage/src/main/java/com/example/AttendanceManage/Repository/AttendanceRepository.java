package com.example.AttendanceManage.Repository;

import com.example.AttendanceManage.model.Attendance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AttendanceRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private Attendance attendance;

    public List<Attendance> getAllAttendance(){
        return null;
    }

    public Attendance getAttendanceById(){
        return null;
    }

    public boolean addAttendance(){
        return true;
    }

}
