package com.example.AttendanceManage.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// http://localhost:8080/
// http://localhost:8080/
//接続時にPlease Sign in か Loginのどっちか書いてあるか確認してからやれ

//error時の動き決めて無いっけ？
@Controller
public class GreetingController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        //ログイン
        //接続確認用
        //System.out.println("test");
        return "login";
    }

    @GetMapping("/manage")
    public String ManageMenu(){
        //管理者メイン
        return "manager_menu";
    }

    @GetMapping("/manage/userEdit")
    public String UserEdit(){
        //ユーザー編集
        //編集後の処理は何処に？
        return "user_edit";
    }

    @GetMapping("/manage/workTime")
    public String WorkTimeList(){
        //勤務時間一覧
        //一覧の情報を受け取って(関数名)ページ表示
        return "workers_list";

    }

    @GetMapping("/user")
    public String UserMenu(){
        //ユーザーメニュー
        return "user_menu";
    }

    @GetMapping("/user/attendanceList")
    public String WorkerList(){
        //勤務状況一覧
        return "attendance_list";
    }

    @GetMapping("/user/addressChange")
    public String AddressChange(){
        return "address_change";
    }

    @GetMapping("/user/attendance")
    public String Attendance(){ return "index";}
/*
    @PostMapping("/user")
    public String confirm(@RequestParam String message,String password,Model model) {
        System.out.println("hello");

        return"user_menu";
    }
*/
}

