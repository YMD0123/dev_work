package com.example.AttendanceManage.Repository;

import com.example.AttendanceManage.model.Attendance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class AttendanceRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Attendance> getAllAttendance(String department_code){
        String sql = "SELECT * FROM attendance WHERE department_code = ?;";
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

    public boolean clockingIn(String place, int user_id) {

        String sql = "INSERT INTO attendance (user_id, date, start_time, end_time, location) " +
                "VALUES (?, ?, ?::time, ?::time, ?)";
        String getClockInTime;

        // 出勤日付取得
        LocalDate days = LocalDate.now();
        System.out.println("出勤日時:" + days);
        System.out.println("勤務場所:" + place);

        try {
            // 現在時間取得
            getClockInTime = getNowTime();
            System.out.println("出勤時間:" + getClockInTime);
            jdbcTemplate.update(sql, user_id,days, getClockInTime, getClockInTime, place);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean clockingOut(int user_id) {

        //退勤ボタンが押されたとき
        String sql = "UPDATE attendance SET end_time = ?::time WHERE id = ?";
        String getClockOutTime;

        try {
            // 現在時間取得
            getClockOutTime = getNowTime();
            System.out.println("退勤時間:" + getClockOutTime);
            jdbcTemplate.update(sql, getClockOutTime, user_id);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean startBreak() {

        //休憩入りボタンが押されたとき
        // TODO 休憩開始ボタン押されてから休憩週力ボタンを押されてからに時間を格納

        return true;
    }

    public boolean endBreak() {

        //休憩終わりボタンが押されたとき

        return true;
    }


    public String getNowTime() {
        Date nowDate = new Date();
        // フォーマット指定
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String nowTime = sdf.format(nowDate);

        return nowTime;
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
