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
    public String clockingInput(HttpSession session, @RequestParam("place") String place, Model model) {

        if (session.getAttribute( "userId") != null) {

            int attendanceId = attendanceRepository.findAttendanceIdByUser((int) session.getAttribute("userId"));

            if (attendanceId == 0) {
                boolean isResult = attendanceRepository.clockingIn(place, attendanceId,
                        (String) session.getAttribute("department_code"));

                if (isResult) {
                    session.setAttribute("working", true);
                    return "redirect:/index";
                } else {
                    return "redirect:/index";
                }
            } else if (attendanceId == -1) {
                System.out.println("error");
                return "redirect:/index";
            } else {
                model.addAttribute("errorMsg", "出勤済みです。");
                return "redirect:/index";
            }
        }
        return "redirect:/login";
    }

    @PostMapping("clockingOut")
    public String clockingOut(HttpSession session, Model model) {

        if (session.getAttribute( "userId") != null) {

            int attendanceId = attendanceRepository.findAttendanceIdByUser((int) session.getAttribute("userId"));

            if (attendanceId != 0) {
                String isWorkingStatus = attendanceRepository.attendanceStatusById(attendanceId);
                System.out.println("状態  :" + isWorkingStatus);

                if (isWorkingStatus.equals("出勤中")) {
                    boolean isResult = attendanceRepository.clockingOut(attendanceId);
                    if (isResult) {
                        session.removeAttribute("working");
                    } else {
                        return "redirect:/index";
                    }
                } else if (isWorkingStatus.equals("休憩中")) {
                    model.addAttribute("errorMsg", "休憩中です。");
                    return "index";
                }
            } else {
                model.addAttribute("errorMsg", "未出勤です。");
                return "redirect:/index";
            }
        }
        return "redirect:/login";
    }

    @PostMapping("startBreak")
    public String startBreak(HttpSession session, Model model) {

        if (session.getAttribute( "userId") != null) {

            int attendanceId = attendanceRepository.findAttendanceIdByUser((int) session.getAttribute("userId"));

            if (attendanceId != 0) {
                String isWorkingStatus = attendanceRepository.attendanceStatusById(attendanceId);
                System.out.println("状態  :" + isWorkingStatus);
                if (isWorkingStatus.equals("出勤中")) {
                    boolean isResult = attendanceRepository.startBreak((int) session.getAttribute("userId"));

                    if (isResult) {
                        session.setAttribute("working", true);
                        return "redirect:/index";
                    } else {
                        return "redirect:/index";
                    }
                } else if (isWorkingStatus.equals("未出勤")) {
                    System.out.println("すでに休憩中です。");
                } else if (isWorkingStatus.equals("休憩中")) {
                    System.out.println("未出勤状態です。");
                } else {
                    return "redirect:/index";
                }
            } else {
                return "redirect:/index";
            }
        }
        return "redirect:/login";
    }

    @PostMapping("endBreak")
    public String endBreak(HttpSession session, Model model) {

        if (session.getAttribute( "userId") != null) {
            boolean isResult = attendanceRepository.endBreak((int) session.getAttribute("userId"));

            if (isResult) {
                session.setAttribute("working", true);
                return "redirect:/index";
            } else {
                return "redirect:/index";
            }
        }
        return "redirect:/login";
    }
}
