package com.example.AttendanceManage.controller;

import com.example.AttendanceManage.Repository.UserRepository;
import com.example.AttendanceManage.model.User;
import jakarta.servlet.http.HttpSession;
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
    public String loginPage(HttpSession session) {
        session.invalidate();
        return "login";
    }

    @PostMapping("/login")
    public String login(HttpSession session,
                        @RequestParam("userid") int userId,
                        @RequestParam("password") String password,
                        Model model) {

        boolean loginResult = userRepository.Login(userId, password);

        if (loginResult) {

            //User情報を取得しセッションにセットする
            User user = userRepository.getUserInfo(userId);

            //session処理
            session.setAttribute("userId",user.getId());
            session.setAttribute("username",user.getUsername());
            session.setAttribute("role",user.getRole());
            session.setAttribute("department_code",user.getDepartmentCode());

            //仮出力
            System.out.println("userId          : " + session.getAttribute("userId"));
            System.out.println("username        : " + session.getAttribute("username"));
            System.out.println("role            : " + session.getAttribute("role"));
            System.out.println("department_code : " + session.getAttribute("department_code"));

            // ログイン成功の場合、index.html に遷移
            return "redirect:/index";
        } else {
            // ログイン失敗の場合、ログイン画面にエラーメッセージを表示
            model.addAttribute("error", "ユーザー名またはパスワードが違います。");
            return "login";
        }
    }

    @RequestMapping("/index")
    public String indexPage(HttpSession session, Model model) {
        boolean working = session.getAttribute("working") != null && (boolean) session.getAttribute("working");

        model.addAttribute("working", working);

        session.removeAttribute("working");

        model.addAttribute("username", session.getAttribute("username"));
        return "index";
    }
}
