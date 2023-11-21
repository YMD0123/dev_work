package com.example.AttendanceManage.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// http://localhost:8080/
@Controller
public class GreetingController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        //String sql = "SELECT * FROM attendances";
        //System.out.println(jdbcTemplate.queryForList(sql));
        return "index";

    }


    @RequestMapping("/manage")
    public String ManageMenu(){
        return "manager_menu";
    }

    @RequestMapping("/manage/userEdit")
    public String UserEdit(){
        return "user_edit";
    }

    @RequestMapping("/manage/workTime")
    public String WorkTimeList(){
        return "worktime_list";
    }

    @RequestMapping("/user")
    public String UserMenu(){
        return "user_menu";
    }

    @RequestMapping("/user/workerList")
    public String WorkerList(){
        return "workers_list";
    }

    @RequestMapping("/user/addressChange")
    public String AddressChange(){
        return "address_change";
    }



}

