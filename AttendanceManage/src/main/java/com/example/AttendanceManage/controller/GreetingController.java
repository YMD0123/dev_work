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
@Controller
public class GreetingController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        System.out.println("test");
        return "index";

    }

    @GetMapping("/login")
    public String login_test(){
        return "index";
    }

    @GetMapping("/manage")
    public String ManageMenu(){
        return "manager_menu";
    }

    @GetMapping("/manage/userEdit")
    public String UserEdit(){
        return "user_edit";
    }

    @GetMapping("/manage/workTime")
    public String WorkTimeList(){
        return "worktime_list";
    }

    @GetMapping("/user")
    public String UserMenu(){
        return "user_menu";
    }

    @GetMapping("/user/workerList")
    public String WorkerList(){
        return "workers_list";
    }

    @GetMapping("/user/addressChange")
    public String AddressChange(){
        return "address_change";
    }

    @PostMapping("/user")
    public String confirm(/*@RequestParam String message,String password,Model model*/) {
        System.out.println("hello");

        return"user_menu";
    }

}

