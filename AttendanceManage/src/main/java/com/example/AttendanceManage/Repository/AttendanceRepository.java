package com.example.AttendanceManage.Repository;

import com.example.AttendanceManage.model.Attendance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.time.YearMonth;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class AttendanceRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String,Object>> getTodayAttendance(String department_code) {
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

    public boolean clockingIn(String place, int userId, String department_code) {

        String sql = "INSERT INTO attendance (user_id, date, start_time, department_code, location, status) " +
                "VALUES (?, ?, ?::time, ?, ?, '出勤中')";
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

    public boolean clockingOut(int attendanceId) {

        String sql = "UPDATE attendance SET end_time = ?::time WHERE id = ?";
        String clockOutTime;

        try {
            // 現在時間取得
            clockOutTime = getNowTime();
            System.out.println("退勤時刻　　　:　" + clockOutTime);
            updateAttendanceStatus("未出勤", attendanceId);
            jdbcTemplate.update(sql, clockOutTime, attendanceId);
            getWorkTime(clockOutTime, attendanceId);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean startBreak(int attendanceId) {

        String sql = "UPDATE attendance SET break_start_time = ?::time WHERE id = ?";
        String startBreakTime;

        try {
            // 現在時間取得
            startBreakTime = getNowTime();
            System.out.println("休憩開始時刻　:　" + startBreakTime);
            updateAttendanceStatus("休憩中", attendanceId);
            jdbcTemplate.update(sql, startBreakTime, attendanceId);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean endBreak(int attendanceId) {

        String sql = "UPDATE attendance SET break_end_time = ?::time WHERE id = ?";
        String endBreakTime;

        try {
            // 現在時間取得
            endBreakTime = getNowTime();
            System.out.println("休憩終了時刻　:　" + endBreakTime);
            updateAttendanceStatus("出勤中", attendanceId);
            jdbcTemplate.update(sql, endBreakTime, attendanceId);
            // 休憩時間取得しDB格納
            getBreakTime(endBreakTime, attendanceId);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void getWorkTime(String clockOutTime, int attendanceId) {

        String sql = "SELECT start_time FROM attendance WHERE id = ?";
        String clockInTime;
        Time   timeWorkTime;
        String workTime;

        try {
            clockInTime = jdbcTemplate.queryForObject(sql, String.class, attendanceId);
            // フォーマット指定,date変換
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            Date dateEndTime = format.parse(clockOutTime);
            Date dateStartTime = format.parse(clockInTime);
            // 合計労働時間計算
            long breakTimeCalc = dateEndTime.getTime() - dateStartTime.getTime();
            // 差分をTime型に変換
            timeWorkTime = new Time(breakTimeCalc);
            // HHを満たさない場合00で埋める
            workTime = new SimpleDateFormat("HH:mm:ss").format(timeWorkTime);
            workTime = "00" + workTime.substring(2);

            System.out.println("合計出勤時間  　　　:　" + workTime);
            // DB格納
            updateWorkTime(workTime, attendanceId);
        } catch (Exception e) {
            System.out.println("DATABASE_ERROR");
            e.printStackTrace();
        }
    }

    public void getBreakTime(String endBreakTime, int attendanceId) {

        String sql = "SELECT break_start_time FROM attendance WHERE id = ?";
        String startBreakTime;
        Time   timeBreakTime;
        String breakTime;

        try {
            startBreakTime = jdbcTemplate.queryForObject(sql, String.class, attendanceId);
            System.out.println("startBreakTime   :" + startBreakTime);
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
            updateBreakTime(breakTime, attendanceId);
        } catch (Exception e) {
            System.out.println("DATABASE_ERROR");
        }
    }

    public void updateBreakTime(String breakTime, int attendanceId) {

        String sql = "UPDATE attendance SET break_duration = ? WHERE id = ?";

        try {
            jdbcTemplate.update(sql, Time.valueOf(breakTime), attendanceId);
        } catch (Exception e) {
            System.out.println("DATABASE_ERROR");
        }

    }

    private void updateWorkTime(String workTime, int attendanceId) {

        String sql = "UPDATE attendance SET total_work_time = ? WHERE id = ?";

        try {
            jdbcTemplate.update(sql, Time.valueOf(workTime), attendanceId);
        } catch (Exception e) {
            System.out.println("DATABASE_ERROR");
        }
    }

    public void updateAttendanceStatus (String status, int attendanceId) {

        String sql = "UPDATE attendance SET status = ? WHERE id = ?";

        try {
            // 呼び出し元によってstatusの値は異なる
            jdbcTemplate.update(sql, status, attendanceId);
        } catch (Exception e) {
            System.out.println("DATABASE_ERROR");
        }
    }

    public String attendanceStatusById (int attendanceId) {

        // 出勤されてないときはuserStatusにnullが入るのでその対応
        String sql = "SELECT status FROM attendance WHERE id = ?";
        String userStatus = "未出勤";

        try {
            userStatus =  jdbcTemplate.queryForObject(sql, String.class, attendanceId);
        } catch (Exception e) {
            System.out.println("DATABASE_ERROR");
        }
        return userStatus;
    }

    public int findAttendanceIdByUser(int userId){
        //現在の日時
        LocalDate today = LocalDate.now();
        String sql = "SELECT id FROM attendance WHERE user_id = ? AND date = ?";
        List<Integer> attendanceList;
        int attendanceId;
        //抽出処理
        try {
            attendanceList = jdbcTemplate.queryForList(sql, Integer.class, userId, today);
            System.out.println("Attendance ID : " + attendanceList);
        }catch(Exception e){
            return -1;
        }
        //複数件抽出
        if(attendanceList.size() > 1){
            attendanceId = -1;
        //抽出結果無し
        }else if(attendanceList.isEmpty()){
            attendanceId = 0;
        }else{
            attendanceId=  attendanceList.get(0);
        }
        return attendanceId;
    }

    private String getNowTime() {

        Date nowDate = new Date();
        // フォーマット指定
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        return sdf.format(nowDate);
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

    public List<Map<String,Object>> getAttendanceHistory(int userId){
        //userIdでAttendanceテーブルの値を取得する
        YearMonth currentYearMonth = YearMonth.now();
        // 現在の月の最初の日と最後の日を取得
        LocalDate firstDayOfMonth = currentYearMonth.atDay(1);
        LocalDate lastDayOfMonth = currentYearMonth.atEndOfMonth();
        List<Map<String,Object>> att_map = null;
        try{
            String sql = "SELECT * FROM attendance WHERE user_id = ? AND date >= ?::date AND date <= ?::date;";
            att_map = jdbcTemplate.queryForList(sql,userId,firstDayOfMonth,lastDayOfMonth);
            System.out.println(att_map);
        }catch(Exception e){
            e.printStackTrace();
        }

        return att_map;
    }
}