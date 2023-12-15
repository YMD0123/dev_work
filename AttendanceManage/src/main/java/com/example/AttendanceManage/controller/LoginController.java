package com.example.AttendanceManage.controller;

import com.example.AttendanceManage.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") int userId,
                        @RequestParam("password") String password,
                        Model model) {

        if (userId != 0 && password != null) {

            boolean loginResult = userRepository.Login(userId, password);

            if (loginResult) {
                return "index";
            } else {
                model.addAttribute("error", "ユーザー名またはパスワードが違います。");
                return "login";
            }
        }
        return "login";
    }

    @RequestMapping("/index")
    public String indexPage() {
        return "index";
    }
}
