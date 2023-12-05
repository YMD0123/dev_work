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

        boolean Result = attendanceRepository.clockingIn(place, (int)session.getAttribute( "userId"));

        if (Result) {
            model.addAttribute("InOutMsg", "今日も頑張りましょう！！");
            return "index";
        } else {
            // 登録エラーメッセージアラート表示
            model.addAttribute("errorMsg", "登録エラー");
            return "index";
        }
    }

    @PostMapping("clockingOut")
    public String clockingout(HttpSession session, Model model) {

        boolean Result = attendanceRepository.clockingOut((int)session.getAttribute( "userId"));

        if (Result) {
            model.addAttribute("InOutMsg", "本日もお疲れ様でした！！");
            return "index";
        } else {
            model.addAttribute("errorMsg", "登録エラー");
            return "index";
        }
    }

    @PostMapping("startBreak")
    public String startBreak(HttpSession session, Model model) {


        boolean Result = attendanceRepository.startBreak((int)session.getAttribute( "userId"));

        if (Result) {
            model.addAttribute("BreakMsg", "休憩開始！！");
            return "index";
        } else {
            model.addAttribute("errorMsg", "登録エラー");
            return "index";
        }
    }

    @PostMapping("endBreak")
    public String endBreak(HttpSession session, Model model) {

        boolean Result = attendanceRepository.endBreak((int)session.getAttribute( "userId"));

        if (Result) {
            model.addAttribute("BreakMsg", "休憩終了！！");
            return "index";
        } else {
            model.addAttribute("errorMsg", "登録エラー");
            return "index";
        }
    }
}
