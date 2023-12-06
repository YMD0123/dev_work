package com.example.AttendanceManage.Repository;

import com.example.AttendanceManage.model.Attendance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Time;
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

    public boolean clockingIn(String place, int userId) {

        String sql = "INSERT INTO attendance (user_id, date, start_time, end_time, location) " +
                "VALUES (?, ?, ?::time, ?::time, ?)";
        String getClockInTime;

        // 出勤日付取得
        LocalDate days = LocalDate.now();
        System.out.println("出勤日付　　　:" + days);
        System.out.println("勤務場所　　　:" + place);

        try {
            // 現在時間取得
            getClockInTime = getNowTime();
            System.out.println("出勤時刻　　　:" + getClockInTime);
            jdbcTemplate.update(sql, userId, days, getClockInTime, getClockInTime, place);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean clockingOut(int userId) {

        String sql = "UPDATE attendance SET end_time = ?::time WHERE id = ?";
        String getClockOutTime;

        try {
            // 現在時間取得
            getClockOutTime = getNowTime();
            System.out.println("退勤時刻　　　:" + getClockOutTime);
            jdbcTemplate.update(sql, getClockOutTime, userId);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean startBreak(int userId) {

        String sql = "UPDATE attendance SET break_start_time = ?::time WHERE id = ?";
        String getStartBreak;

        try {
            // 現在時間取得
            getStartBreak = getNowTime();
            System.out.println("休憩開始時刻　:" + getStartBreak);
            jdbcTemplate.update(sql, getStartBreak, userId);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean endBreak(int userId) {

        String sql = "UPDATE attendance SET break_end_time = ?::time WHERE id = ?";
        String getEndBreak;

        try {
            // 現在時間取得
            getEndBreak = getNowTime();
            System.out.println("休憩終了時刻　:" + getEndBreak);
            jdbcTemplate.update(sql, getEndBreak, userId);
            // 休憩時間取得しDB格納
            getBreakTime(getEndBreak, userId);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public String getNowTime() {

        Date nowDate = new Date();
        // フォーマット指定
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        return sdf.format(nowDate);
    }

    public void getBreakTime(String getEndBreak, int userId) {

        String getBreakTimeSql = "SELECT break_start_time FROM attendance WHERE id = ?";
        String sql = "UPDATE attendance SET break_duration = ? WHERE id = ?";
        String getStartTime;
        String getBreakTime;
        Time   BreakTime;

        try {
            getStartTime = jdbcTemplate.queryForObject(getBreakTimeSql, String.class, userId);
            // フォーマット指定,date変換
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            Date endBreakTime = format.parse(getEndBreak);
            Date startBreakTime = format.parse(getStartTime);
            // 休憩開始と終了差分計算
            long getTime = endBreakTime.getTime() - startBreakTime.getTime();
            // 差分をTime型に変換
            BreakTime = new Time(getTime);
            // HHを満たさない場合00で埋める
            getBreakTime = new SimpleDateFormat("HH:mm:ss").format(BreakTime);
            getBreakTime = "00" + getBreakTime.substring(2);

            System.out.println("休憩時間　　　:" + getBreakTime);
            jdbcTemplate.update(sql, Time.valueOf(getBreakTime), userId);
        } catch (Exception e) {
            System.out.println("DATABASE_ERROR");
        }
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
        if (row.get("break_duration") != null) attendance.setEndTime(((java.sql.Time)row.get("break_duration")).toLocalTime());
        return attendance;
    }
}