package com.example.AttendanceManage.controller;

import com.example.AttendanceManage.Repository.AttendanceRepository;
import com.example.AttendanceManage.Repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

// http://localhost:8080/
// http://localhost:8080/
//接続時にPlease Sign in か Loginのどっちか書いてあるか確認してからやれ

//error時の動き決めて無いっけ？
@Controller
public class GreetingController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {

        //TODO session idが空の時ログインにリダイレクトを行いURLでのアクセスを禁止する
        model.addAttribute("name", name);
        //ログイン
        //接続確認用
        //System.out.println("test");
        return "login";
    }

    @GetMapping("/manage")
    public String ManageMenu(){

        //TODO session idが空の時ログインにリダイレクトを行いURLでのアクセスを禁止する
        //管理者メイン
        return "manager_menu";
    }

    @GetMapping("/manage/userEdit")
    public String UserEdit(){

        //TODO session idが空の時ログインにリダイレクトを行いURLでのアクセスを禁止する
        //ユーザー編集
        //編集後の処理は何処に？
        return "user_edit";
    }

    @GetMapping("/manage/workTime")
    public String WorkTimeList(){

        //TODO session idが空の時ログインにリダイレクトを行いURLでのアクセスを禁止する
        //勤務時間一覧
        //一覧の情報を受け取って(関数名)ページ表示
        return "workers_list";

    }

    @GetMapping("/user")
    public String UserMenu(){

        //TODO session idが空の時ログインにリダイレクトを行いURLでのアクセスを禁止する
        //ユーザーメニュー
        return "user_menu";
    }

    @GetMapping("/user/attendanceList")
    public String WorkerList(HttpSession session,Model model){

        //TODO session idが空の時ログインにリダイレクトを行いURLでのアクセスを禁止する
        //当日の同部署コードの勤務状況一覧
        List<Map<String,Object>> list = attendanceRepository.getTodayAttendance((String) session.getAttribute("department_code"));
        model.addAttribute("attendancelist", list);
        return "attendance_list";
    }

    @GetMapping("/user/addressChange")
    public String AddressChange(){

        //TODO session idが空の時ログインにリダイレクトを行いURLでのアクセスを禁止する
        return "address_change";
    }

    @GetMapping("/user/attendance")
    public String Attendance(){
        //TODO session idが空の時ログインにリダイレクトを行いURLでのアクセスを禁止する
        return "index";
    }

/*
    @PostMapping("/user")
    public String confirm(@RequestParam String message,String password,Model model) {
        System.out.println("hello");

        return"user_menu";
    }
*/
    @RequestMapping("/address_change")
    public String testView(){
        return "address_change";
    }
    @PostMapping("/address_change")
    public String testInput(HttpSession session,
                            @RequestParam("email") String email,
                            @RequestParam("phonenumber") String phonenumber,
                            Model model){

        boolean isTestResult = userRepository.phoneAddress((int)session.getAttribute( "userId"),email, phonenumber);

        return "address_change";
    }
}

