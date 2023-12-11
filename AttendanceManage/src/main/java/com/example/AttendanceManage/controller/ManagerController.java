package com.example.AttendanceManage.controller;

import com.example.AttendanceManage.Repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ManagerController {

    @Autowired
    ManagerRepository managerRepository;
    private JdbcTemplate jdbcTemplate;

    @RequestMapping("/manager")
    public String indexView() {
        //TODO session idが空の時ログインにリダイレクトを行いURLでのアクセスを禁止する
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
                           @RequestParam("role") String role,
                           @RequestParam("department_code") String department_code, Model model){

        //TODO session idが空の時ログインにリダイレクトを行いURLでのアクセスを禁止する

        boolean isAddResult = managerRepository.userInsert(username, password, role, department_code);

        if (isAddResult) {
            return "redirect:/user_add";
        }
        model.addAttribute("errorMsg","登録エラー");
        return  "redirect/user_add";
    }

    @GetMapping("/{id}")
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
}
