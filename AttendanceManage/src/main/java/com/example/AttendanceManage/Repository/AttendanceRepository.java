package com.example.AttendanceManage.Repository;

import com.example.AttendanceManage.model.Attendance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class AttendanceRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Attendance> getAllAttendance(String department_code){
        String sql = "SELECT * from attendance where department_code = ?;";
        List<Map<String,Object>> att_map = jdbcTemplate.queryForList(sql,department_code);
        System.out.println(att_map);
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

    public boolean clockingIn(){

        //出勤ボタンが押されたとき

        return true;
    }

    public boolean clockingOut(){

        //退勤ボタンが押されたとき

        return true;
    }

    public boolean startBreak(){

        //休憩入りボタンが押されたとき

        return true;
    }

    public boolean endBreak(){

        //休憩終わりボタンが押されたとき

        return true;
    }


    private Attendance mapToAttendance(Map<String, Object> row) {
        Attendance attendance = new Attendance();
        attendance.setId((Integer)row.get("id"));
        attendance.setUserId((Integer)row.get("user_id"));
        attendance.setDepartmentCode((String)row.get("department_code"));
        // Date型からLocalDate型への変換
        attendance.setDate(((java.sql.Date)row.get("date")).toLocalDate());
        // Time型からLocalTime型への変換
        attendance.setStartTime(((java.sql.Time)row.get("start_time")).toLocalTime());
        attendance.setEndTime(((java.sql.Time)row.get("end_time")).toLocalTime());
        attendance.setBreakDuration((Integer)row.get("break_duration"));
        return attendance;
    }
}
