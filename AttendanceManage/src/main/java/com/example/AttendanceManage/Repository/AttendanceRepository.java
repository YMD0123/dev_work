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

    public List<Map<String,Object>> getTodaysAttendance(String department_code) {
        //現在の日時
        LocalDate today = LocalDate.now();
        //String sql = "SELECT * FROM attendance WHERE department_code = ? AND date = ?;";
        String sql = "SELECT a.*, u.username, u.department_code AS user_department_code, u.phone_number, u.email " +
                "FROM attendance a " +
                "INNER JOIN users u ON a.user_id = u.id " +
                "WHERE a.department_code = ? AND a.date = ?";
        List<Map<String,Object>> att_map = jdbcTemplate.queryForList(sql,department_code,today);
        System.out.println(att_map);
        return att_map;
    }



    public boolean clockingIn(String place, int userId,String department_code) {

        String sql = "INSERT INTO attendance (user_id, date, start_time, department_code, location) " +
                "VALUES (?, ?, ?::time, ?, ?)";
        String clockInTime;

        // 出勤日付取得
        LocalDate days = LocalDate.now();
        System.out.println("出勤日付　　　:　" + days);
        System.out.println("勤務場所　　　:　" + place);

        try {
            // 現在時間取得
            clockInTime = getNowTime();
            System.out.println("出勤時刻　　　:　" + clockInTime);
            jdbcTemplate.update(sql, userId, days, clockInTime, department_code, place);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean clockingOut(int userId) {

        String sql = "UPDATE attendance SET end_time = ?::time WHERE user_id = ?";
        //String sql = "UPDATE attendance SET end_time = ?::time WHERE id = ?";
        String clockOutTime;

        try {
            // 現在時間取得
            clockOutTime = getNowTime();
            System.out.println("退勤時刻　　　:　" + clockOutTime);
            jdbcTemplate.update(sql, clockOutTime, userId);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean startBreak(int userId) {

        String sql = "UPDATE attendance SET break_start_time = ?::time WHERE user_id = ?";
        String startBreakTime;

        try {
            // 現在時間取得
            startBreakTime = getNowTime();
            System.out.println("休憩開始時刻　:　" + startBreakTime);
            jdbcTemplate.update(sql, startBreakTime, userId);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean endBreak(int userId) {

        String sql = "UPDATE attendance SET break_end_time = ?::time WHERE user_id = ?";
        String endBreakTime;

        try {
            // 現在時間取得
            endBreakTime = getNowTime();
            System.out.println("休憩終了時刻　:　" + endBreakTime);
            jdbcTemplate.update(sql, endBreakTime, userId);
            // 休憩時間取得しDB格納
            getBreakTime(endBreakTime, userId);
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

    public void getBreakTime(String endBreakTime, int userId) {

        String Sql = "SELECT break_start_time FROM attendance WHERE user_id = ?";
        String startBreakTime;
        Time   timeBreakTime;
        String breakTime;

        try {
            startBreakTime = jdbcTemplate.queryForObject(Sql, String.class, userId);
            // フォーマット指定,date変換
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            Date dateEndTime = format.parse(endBreakTime);
            Date dateStartTime = format.parse(startBreakTime);
            // 休憩開始と終了差分計算
            long breakTimeCalc = dateEndTime.getTime() - dateStartTime.getTime();
            // 差分をTime型に変換
            timeBreakTime = new Time(breakTimeCalc);
            // HHを満たさない場合00で埋める
            breakTime = new SimpleDateFormat("HH:mm:ss").format(timeBreakTime);
            breakTime = "00" + breakTime.substring(2);

            System.out.println("休憩時間　　　:　" + breakTime);
            // DB格納
            updateBreakTime(breakTime, userId);
        } catch (Exception e) {
            System.out.println("DATABASE_ERROR");
        }
    }

    public void updateBreakTime(String breakTime, int userId) {

        String sql = "UPDATE attendance SET break_duration = ? WHERE user_id = ?";

        try {
            jdbcTemplate.update(sql, Time.valueOf(breakTime), userId);
        } catch (Exception e) {
            System.out.println("DATABASE_ERROR");
        }

    }

    public List<Attendance> getTodayWorkStatus(){
        //TODO 出勤している日付が一致するレコードを一覧で取得する
        return null;
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
        if (row.get("end_time") != null)attendance.setEndTime(((java.sql.Time)row.get("end_time")).toLocalTime());
        if (row.get("break_duration") != null) attendance.setEndTime(((java.sql.Time)row.get("break_duration")).toLocalTime());
        return attendance;
    }

    private int findAttendanceIdByUser(int userId){
        //TODO　今日の日付取得
        //TODO  UserIdと日付を条件にAttendanceテーブルのレコードからIDを取得する
        //TODO  結果をint型で返す。該当ない場合は、0を返す
        return 0;
    }
}