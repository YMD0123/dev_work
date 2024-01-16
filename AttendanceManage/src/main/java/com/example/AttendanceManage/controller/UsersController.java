package com.example.AttendanceManage.controller;

import com.example.AttendanceManage.Repository.AttendanceRepository;
import com.example.AttendanceManage.Repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

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

    @RequestMapping("/address_edit")
    public String addresChangeView(Model model, HttpSession session) {

        String loginUserStatus = attendanceRepository.attendanceStatusById(
                attendanceRepository.findAttendanceIdByUser((int) session.getAttribute("userId")));
        String loginUserName = userRepository.findUserNameById((int) session.getAttribute("userId"));

        model.addAttribute("userStatus", loginUserStatus);
        model.addAttribute("userName", loginUserName);
        model.addAttribute("userId", session.getAttribute("userId"));

        model.addAttribute("userAddress", userRepository.findUserAddressById((int)session.getAttribute("userId")));

        return "address_edit";
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

        return "redirect:/address_change";
    }

    @GetMapping("/")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {

        //TODO session idが空の時ログインにリダイレクトを行いURLでのアクセスを禁止する
        model.addAttribute("name", name);
        //ログイン
        //接続確認用
        //System.out.println("test");
        return "login";
    }

    @GetMapping("/manage")
    public String ManageMenu(){

        //TODO session idが空の時ログインにリダイレクトを行いURLでのアクセスを禁止する
        //管理者メイン
        return "manager_menu";
    }

    @GetMapping("/manage/userEdit")
    public String UserEdit(){

        //TODO session idが空の時ログインにリダイレクトを行いURLでのアクセスを禁止する
        //ユーザー編集
        //編集後の処理は何処に？
        return "user_edit";
    }

    @GetMapping("/manage/workTime")
    public String WorkTimeList(){

        //TODO session idが空の時ログインにリダイレクトを行いURLでのアクセスを禁止する
        //勤務時間一覧
        //一覧の情報を受け取って(関数名)ページ表示
        return "workers_list";

    }

    @GetMapping("/user")
    public String UserMenu(){

        //TODO session idが空の時ログインにリダイレクトを行いURLでのアクセスを禁止する
        //ユーザーメニュー
        return "user_menu";
    }

    @GetMapping("/user/attendanceList")
    public String WorkerList(HttpSession session, Model model){

        String loginUserStatus = attendanceRepository.attendanceStatusById(
                attendanceRepository.findAttendanceIdByUser((int) session.getAttribute("userId")));
        String loginUserName = userRepository.findUserNameById((int) session.getAttribute("userId"));

        model.addAttribute("userStatus", loginUserStatus);
        model.addAttribute("userName", loginUserName);
        model.addAttribute("userId", session.getAttribute("userId"));

        //TODO session idが空の時ログインにリダイレクトを行いURLでのアクセスを禁止する
        //当日の同部署コードの勤務状況一覧
        List<Map<String,Object>> list = attendanceRepository.getTodayAttendance((String) session.getAttribute("department_code"));
        if(list != null){
            model.addAttribute("attendancelist", list);
        }else{
            model.addAttribute("errorMsg","エラーが発生しました。");
        }
        return "attendance_list";
    }

    @GetMapping("/user/addressChange")
    public String AddressChange(){

        //TODO session idが空の時ログインにリダイレクトを行いURLでのアクセスを禁止する
        return "address_change";
    }

    @GetMapping("/user/attendance")
    public String Attendance(){
        //TODO session idが空の時ログインにリダイレクトを行いURLでのアクセスを禁止する
        return "index";
    }

    @GetMapping("/user/attendanceHistory")
    public String attendanceHistory(HttpSession session, Model model) {

        String loginUserStatus = attendanceRepository.attendanceStatusById(
                attendanceRepository.findAttendanceIdByUser((int) session.getAttribute("userId")));
        String loginUserName = userRepository.findUserNameById((int) session.getAttribute("userId"));

        model.addAttribute("userStatus", loginUserStatus);
        model.addAttribute("userName", loginUserName);
        model.addAttribute("userId", session.getAttribute("userId"));

        List<Map<String, Object>> attendanceList = attendanceRepository.getAttendanceHistory((Integer) session.getAttribute("userId"));
        if (attendanceList != null) {
            model.addAttribute("attendancelist", attendanceList);
        } else {
            model.addAttribute("errorMsg", "エラーが発生しました。");
        }

        return "attendance_history";
    }

    @RequestMapping("/user/address_change")
    public String testView(HttpSession session, Model model){

        String loginUserStatus = attendanceRepository.attendanceStatusById(
                attendanceRepository.findAttendanceIdByUser((int) session.getAttribute("userId")));
        String loginUserName = userRepository.findUserNameById((int) session.getAttribute("userId"));

        model.addAttribute("userStatus", loginUserStatus);
        model.addAttribute("userName", loginUserName);
        model.addAttribute("userId", session.getAttribute("userId"));

        return "address_change";
    }
    @PostMapping("/user/address_change")
    public String testInput(HttpSession session,
                            @RequestParam("email") String email,
                            @RequestParam("phonenumber") String phonenumber,
                            Model model){

        boolean isTestResult = userRepository.phoneAddress((int)session.getAttribute( "userId"),email, phonenumber);

        return "address_change";
    }
}
