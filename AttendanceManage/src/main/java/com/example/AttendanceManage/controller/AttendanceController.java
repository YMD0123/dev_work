package com.example.AttendanceManage.controller;

import com.example.AttendanceManage.Repository.AttendanceRepository;
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
    public String clockinginput(@RequestParam("place") String place, Model model) {

        int user_id = 1;

        boolean Result = attendanceRepository.clockingIn(place, user_id);

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
    public String clockingout(Model model) {

        int user_id = 1;

        boolean Result = attendanceRepository.clockingOut(user_id);

        if (Result) {
            model.addAttribute("InOutMsg", "本日もお疲れ様でした！！");
            return "index";
        } else {
            model.addAttribute("errorMsg", "登録エラー");
            return "index";
        }
    }

    @PostMapping("startBreak")
    public String startBreak(Model model) {

        int user_id = 1;

        boolean Result = attendanceRepository.startBreak(user_id);

        if (Result) {
            model.addAttribute("BreakMsg", "休憩開始！！");
            return "index";
        } else {
            model.addAttribute("errorMsg", "登録エラー");
            return "index";
        }
    }

    @PostMapping("endBreak")
    public String endBreak(Model model) {

        int user_id = 1;

        boolean Result = attendanceRepository.endBreak(user_id);

        if (Result) {
            model.addAttribute("BreakMsg", "休憩終了！！");
            return "index";
        } else {
            model.addAttribute("errorMsg", "登録エラー");
            return "index";
        }
    }
}
