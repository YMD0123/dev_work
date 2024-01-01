package com.example.AttendanceManage.controller;

import com.example.AttendanceManage.Repository.AttendanceRepository;
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
    @Autowired
    private AttendanceRepository attendanceRepository;

    @RequestMapping("/login")
    public String loginView(HttpSession session) {
        session.invalidate();
        return "login";
    }

    @PostMapping("/login")
    public String login(HttpSession session,
                        @RequestParam(name = "userid", required = false) Integer userId,
                        @RequestParam(name = "password", required = false) String password,
                        Model model) {

        if (userId != 0 && password != null) {

            boolean isLoginResult = userRepository.Login(userId, password);

            if (isLoginResult) {

                //User情報を取得しセッションにセットする
                User user = userRepository.getUserInfo(userId);

                //session処理
                session.setAttribute("userId", user.getId());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("role", user.getRole());
                session.setAttribute("department_code", user.getDepartmentCode());

                //仮出力
                System.out.println("userId          : " + session.getAttribute("userId"));
                System.out.println("username        : " + session.getAttribute("username"));
                System.out.println("role            : " + session.getAttribute("role"));
                System.out.println("department_code : " + session.getAttribute("department_code"));

                //UserのRoleがadminだったときadminメニューへ遷移
                if (user.getRole().equals("admin")) {
                    System.out.println("go admin menu");
                    return "/manager/manager_menu";
                }

                // ログイン成功の場合、index.html に遷移
                return "redirect:/index";
            } else {
                // ログイン失敗の場合、ログイン画面にエラーメッセージを表示
                model.addAttribute("error", "ユーザー名またはパスワードが違います。");
                return "login";
            }
        } else {
            // ログイン失敗の場合、ログイン画面にエラーメッセージを表示
            model.addAttribute("error", "ユーザー名またはパスワードが違います。");
            if (userId == null || password == null) {
                System.out.println("入力無");
                return "login";
            } else if (userId == null) {
                System.out.println("userIdを入力してください。");
                return "login";
            } else if (password == null) {
                System.out.println("passwordを入力してください。");
                return "login";
            }

            return "login";
        }
    }

    @RequestMapping("/index")
    public String indexView (HttpSession session, Model model) {

        // ログイン中ユーザー表示＆ステータス表示
        String loginUserStatus = attendanceRepository.attendanceStatusById(
                attendanceRepository.findAttendanceIdByUser((int) session.getAttribute("userId")));
        String loginUserName = userRepository.findUserNameById((int) session.getAttribute("userId"));

        model.addAttribute("userStatus", loginUserStatus);
        model.addAttribute("userName", loginUserName);
        model.addAttribute("userId", session.getAttribute("userId"));

        System.out.println("********LoginUser********");
        System.out.println("Status   : " + loginUserStatus);
        System.out.println("Username : " + loginUserName);
        System.out.println("*************************");

        return "index";
    }
}