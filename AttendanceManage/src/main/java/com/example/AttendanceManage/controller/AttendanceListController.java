package com.example.AttendanceManage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
public class AttendanceListController {

    List anser;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/attendanceList")
    public String index(Model model){
        String sql = "SELECT * FROM attendances";

        System.out.println(jdbcTemplate.queryForList(sql));
        anser = jdbcTemplate.queryForList(sql);
        return "attendance_list";
    }


}
