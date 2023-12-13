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

    public List<Attendance> getAllAttendance(String department_code) {
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

    public Attendance getAttendanceById() {
        return null;
    }

    public boolean clockingIn(String place, int userId) {

        String sql = "INSERT INTO attendance (user_id, date, start_time, end_time, location, status) " +
                "VALUES (?, ?, ?::time, ?::time, ?, '出勤中')";
        String clockInTime;

        // 出勤日付取得
        LocalDate days = LocalDate.now();
        System.out.println("出勤日付　　　:　" + days);
        System.out.println("勤務場所　　　:　" + place);

        try {
            // 現在時間取得
            clockInTime = getNowTime();
            System.out.println("出勤時刻　　　:　" + clockInTime);
            jdbcTemplate.update(sql, userId, days, clockInTime, clockInTime, place);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean clockingOut(int userId) {

        String sql = "UPDATE attendance SET end_time = ?::time WHERE id = ?";
        String clockOutTime;

        try {
            // 現在時間取得
            clockOutTime = getNowTime();
            System.out.println("退勤時刻　　　:　" + clockOutTime);
            updateAttendanceStatus("未出勤",userId);
            jdbcTemplate.update(sql, clockOutTime, userId);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean startBreak(int userId) {

        String sql = "UPDATE attendance SET break_start_time = ?::time WHERE id = ?";
        String startBreakTime;

        try {
            // 現在時間取得
            startBreakTime = getNowTime();
            System.out.println("休憩開始時刻　:　" + startBreakTime);
            updateAttendanceStatus("休憩中",userId);
            jdbcTemplate.update(sql, startBreakTime, userId);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean endBreak(int userId) {

        String sql = "UPDATE attendance SET break_end_time = ?::time WHERE id = ?";
        String endBreakTime;

        try {
            // 現在時間取得
            endBreakTime = getNowTime();
            System.out.println("休憩終了時刻　:　" + endBreakTime);
            updateAttendanceStatus("出勤中",userId);
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

        String Sql = "SELECT break_start_time FROM attendance WHERE id = ?";
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

        String sql = "UPDATE attendance SET break_duration = ? WHERE id = ?";

        jdbcTemplate.update(sql, Time.valueOf(breakTime), userId);
    }

    public void updateAttendanceStatus (String status, int userId) {

        String sql = "UPDATE attendance SET status = ? WHERE id = ?";

        try {
            // 呼び出し元によってstatusの値は異なる
            jdbcTemplate.update(sql, status, userId);
        } catch (Exception e) {
            System.out.println("DATABASE_ERROR");
        }
    }

//    public String attendanceStatusById (int userId) {
//// 出勤されてないときはuserStatusにnullが入るのでその対応
//        String sql = "SELECT status FROM attendance WHERE id = ?";
//        String userStatus = null;
//
//        try {
//            userStatus =  jdbcTemplate.queryForObject(sql, String.class, userId);
//        } catch (Exception e) {
//            System.out.println("DATABASE_ERROR");
//        }
//        return userStatus;
//    }

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