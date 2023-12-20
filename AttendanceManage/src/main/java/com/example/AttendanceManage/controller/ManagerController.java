package com.example.AttendanceManage.controller;

import com.example.AttendanceManage.Repository.AttendanceRepository;
import com.example.AttendanceManage.Repository.ManagerRepository;
import com.example.AttendanceManage.Repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ManagerController {

    @Autowired
    ManagerRepository managerRepository;
    @Autowired
    AttendanceRepository attendanceRepository;
    @Autowired
    UserRepository userRepository;
    private JdbcTemplate jdbcTemplate;

    @RequestMapping("/manager_menu")
    public String indexView(HttpSession session, Model model) {
        //TODO session idが空の時ログインにリダイレクトを行いURLでのアクセスを禁止する

        // ログイン中ユーザー表示＆ステータス表示
        String userStatus =  attendanceRepository.attendanceStatusById(
                attendanceRepository.findAttendanceIdByUser((int) session.getAttribute("userId")));
        String userName = userRepository.findUserNameById((int) session.getAttribute("userId"));

        model.addAttribute("userStatus", userStatus);
        model.addAttribute("userName", userName);

        System.out.println("********LoginUser********");
        System.out.println("Status   : " + userStatus);
        System.out.println("UserName : " + userName);
        System.out.println("*************************");


        return "manager/manager_menu";
    }

    @RequestMapping("/user_add")
    public String userAddView() {
        //TODO session idが空の時ログインにリダイレクトを行いURLでのアクセスを禁止する
        return "manager/user_add";
    }

    @GetMapping("/user_list")
    public String usersList(Model model) {
        //TODO session idが空の時ログインにリダイレクトを行いURLでのアクセスを禁止する
        model.addAttribute("users", managerRepository.findAll());
        return "manager/user_list";
    }

    @PostMapping("/user_add")
    public String userAdd(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam("TwoPassword") String TwoPassword,
                           @RequestParam("role") String role,
                           @RequestParam("department_code") String department_code,
                          Model model) {

        //TODO session idが空の時ログインにリダイレクトを行いURLでのアクセスを禁止する

        if (password.equals(TwoPassword)) {
            boolean isAddResult = managerRepository.userInsert(username, password, role, department_code);
            if (isAddResult) {
                return "redirect:/user_add";
            }
            model.addAttribute("errorMsg","登録エラー");
        } else {
            System.out.println("確認用のパスワードと一致しません。");
        }
        return  "redirect:/user_add";
    }

    @GetMapping("edit/{id}")
    public String userEditView(@PathVariable int id, Model model) {

        //TODO session idが空の時ログインにリダイレクトを行いURLでのアクセスを禁止する

        model.addAttribute("user", managerRepository.userEditView(id));
        return "manager/user_edit";
    }

    @GetMapping("delete_user/{id}")
    public String deleteUser(@PathVariable int id, Model model) {

        //TODO session idが空の時ログインにリダイレクトを行いURLでのアクセスを禁止する

        boolean isDeleteResult = managerRepository.userDelete(id);

        if (isDeleteResult) {
            return "redirect:/user_list";
        } else {
            model.addAttribute("errorMsg", "削除失敗");
            return "redirect:/{id}";
        }
    }

    @PostMapping("/user_edit/{id}")
    public String userEdit(@RequestParam("username") String username,
                           @RequestParam("role") String role,
                           @RequestParam("departmentCode") String department_code,
                           @PathVariable int id) {

        boolean isEditResult = managerRepository.userUpdate(username, role, department_code, id);
        if (isEditResult) {
            return "redirect:/user_list";
        }
        return "redirect:/edit/{id}";
    }

    @GetMapping("/user_search")
    public String userSearch(@RequestParam("userid") int userId,
                             @RequestParam("username") String userName,
                             @RequestParam("role") String role,
                             @RequestParam("department_code") String departmentCode,
                             Model model) {

        model.addAttribute("users", managerRepository.userSearch(userId, userName, role, departmentCode));

        return "redirect:/user_list";
    }


}
