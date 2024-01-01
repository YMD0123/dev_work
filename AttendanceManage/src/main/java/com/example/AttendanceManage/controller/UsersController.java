package com.example.AttendanceManage.controller;

import com.example.AttendanceManage.Repository.AttendanceRepository;
import com.example.AttendanceManage.Repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UsersController {


    @Autowired
    AttendanceRepository attendanceRepository;
    @Autowired
    UserRepository userRepository;

    @RequestMapping("/userPassChange")
    public String ChangePageView(HttpSession session, Model model) {
        return "password_change";
    }

    @RequestMapping("/addres_change")
    public String addresChangeView() {

        return "addres_change";
    }

    @PostMapping("/passChange")
    public String passChange(@RequestParam("oldPassword") String oldPassword,
                             @RequestParam("newPassword") String newPassword,
                             @RequestParam("newTwoPassword") String newTwoPassword,
                             HttpSession session, Model model) {

        System.out.println("旧パスワード" + oldPassword);
        System.out.println("新しいパスワード" + newPassword);
        System.out.println("確認用パスワード" + newTwoPassword);


        // ログインしているユーザーが設定しているパスワードと入力されたoldPasswordが一致するか
        boolean isMatchResult = userRepository.Login((int)session.getAttribute("userId") , oldPassword);

        if (isMatchResult && newPassword.equals(newTwoPassword)) {

            boolean canUpdatePw = userRepository.updatePassword(newPassword, (int) session.getAttribute("userId"));

            model.addAttribute("resultMsg", "変更完了です。");

            return "password_change";
        }

        model.addAttribute("errorMsg", "パスワードが一致しません。");

        return "password_change";
    }

    @PostMapping("/updateaddres")
    public String updateChange() {

        return "redirect:/addres_change";
    }
}
