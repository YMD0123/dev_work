package com.example.AttendanceManage.Repository;

import com.example.AttendanceManage.model.Attendance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class AttendanceRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private Attendance attendance;

    public List<Attendance> getAllAttendance(String department_code){
        String sql = "SELECT * from attendance where department_code = ?";
        List<Map<String,Object>> att_map = jdbcTemplate.queryForList(sql,department_code);
        List<Attendance> attendances = new ArrayList<>();

        for(Map<String,Object> obj : att_map){
            Attendance attendance = mapToAttendance(obj);
            attendances.add(attendance);
        }
        return attendances;
    }

    public Attendance getAttendanceById(){
        return null;
    }

    public boolean addAttendance(){
        return true;
    }

    private Attendance mapToAttendance(Map<String, Object> row) {
        Attendance attendance = new Attendance();
        attendance.setId((Integer) row.get("id"));
        attendance.setUserId((Integer) row.get("user_id"));
        attendance.setDepartmentCode((String) row.get("department_code"));
        java.sql.Date sqlDate = (java.sql.Date) row.get("date");

        if (sqlDate != null) {attendance.setDate(sqlDate.toLocalDate());}

        java.sql.Time sqlTime;

        sqlTime = (java.sql.Time) row.get("start_time");
        if (sqlTime != null) {attendance.setStartTime(sqlTime.toLocalTime());}

        sqlTime = (java.sql.Time) row.get("end_time");
        if (sqlTime != null) {attendance.setEndTime(sqlTime.toLocalTime());}

        attendance.setBreakDuration((Integer)row.get("break_duration"));

        return null;
    }
}
