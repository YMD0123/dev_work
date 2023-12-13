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
            boolean isResult = attendanceRepository.clockingIn(place, (int)session.getAttribute( "userId"));

            if (isResult) {
                session.setAttribute("working", true);
                return "redirect:/index";
            } else {
                return "redirect:/index";
            }
        }
        return "redirect:/login";
    }

    @PostMapping("clockingOut")
    public String clockingOut(HttpSession session, Model model) {
// TODO isWorkingStatusがnullだった時の処理作成
        if (session.getAttribute( "userId") != null) {
            String isWorkingStatus = attendanceRepository.attendanceStatusById((int) session.getAttribute("userId"));
            System.out.println("状態  :" + isWorkingStatus);

//            if (isWorkingStatus.equals("出勤中")) {
                boolean isResult = attendanceRepository.clockingOut((int) session.getAttribute("userId"));

                if (isResult) {
                    session.removeAttribute("working");
                } else {
                    return "redirect:/index";
                }
//            } else if (isWorkingStatus.equals("未出勤") || isWorkingStatus.equals("休憩中")) {
//                model.addAttribute("errorMsg", "未出勤または休憩中です。");
//                return "index";
//            }
        }
        return "redirect:/login";
    }

    @PostMapping("startBreak")
    public String startBreak(HttpSession session, Model model) {

        if (session.getAttribute( "userId") != null) {
            boolean isResult = attendanceRepository.startBreak((int) session.getAttribute("userId"));

            if (isResult) {
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
