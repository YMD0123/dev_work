package com.example.AttendanceManage.controller;

import com.example.AttendanceManage.Repository.AttendanceRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AttendanceController {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @PostMapping("/clockingIn")
    public String clockinginput(HttpSession session, @RequestParam("place") String place, Model model) {

        if (session.getAttribute( "userId") != null) {
            boolean Result = attendanceRepository.clockingIn(place, (int)session.getAttribute( "userId"));

            if (Result) {
                session.setAttribute("working", true);
                return "redirect:/index";
            } else {
                return "redirect:/index";
            }
        }
        return "redirect:/login";
    }

    @PostMapping("clockingOut")
    public String clockingout(HttpSession session, Model model) {

        if (session.getAttribute( "userId") != null) {
            boolean Result = attendanceRepository.clockingOut((int) session.getAttribute("userId"));

            if (Result) {
                session.removeAttribute("working");
                return "redirect:/index";
            } else {
                return "redirect:/index";
            }
        }
        return "redirect:/login";
    }

    @PostMapping("startBreak")
    public String startBreak(HttpSession session, Model model) {


        if (session.getAttribute( "userId") != null) {
            boolean Result = attendanceRepository.startBreak((int) session.getAttribute("userId"));

            if (Result) {
                session.setAttribute("working", true);
                return "redirect:/index";
            } else {
                return "redirect:/index";
            }
        }
        return "redirect:/login";
    }

    @PostMapping("endBreak")
    public String endBreak(HttpSession session, Model model) {

        if (session.getAttribute( "userId") != null) {
            boolean Result = attendanceRepository.endBreak((int) session.getAttribute("userId"));

            if (Result) {
                session.setAttribute("working", true);
                return "redirect:/index";
            } else {
                return "redirect:/index";
            }
        }
        return "redirect:/login";
    }
}
