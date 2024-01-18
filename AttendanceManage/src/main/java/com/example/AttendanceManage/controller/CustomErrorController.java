package com.example.AttendanceManage.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @GetMapping("/error")
    public String handleError(Model model) {
        model.addAttribute("errorMsg", "アプリケーション実行中にエラーが発生しました。\nもう一度最初から実行してください。");
        return "login";
    }
}

