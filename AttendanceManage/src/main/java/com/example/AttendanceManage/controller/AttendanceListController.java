package com.example.AttendanceManage.controller;

import com.example.AttendanceManage.Repository.AttendanceRepository;
import com.example.AttendanceManage.model.Attendance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;


@Controller
public class AttendanceListController {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private AttendanceRepository attendanceRepository;

//    @GetMapping("/attendanceList")
//    public String index(Model model){
////        List<Attendance> list = attendanceRepository.getTodaysAttendance("100");
////        model.addAttribute("attendancelist", list);
//        return "attendance_list";
//    }


}
