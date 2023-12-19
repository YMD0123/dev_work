package com.example.AttendanceManage.controller;

import com.example.AttendanceManage.Repository.AttendanceRepository;
import com.example.AttendanceManage.model.Attendance;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class AttendanceController {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @PostMapping("/clockingIn")
    public String clockingInput(HttpSession session, @RequestParam("place") String place, Model model) {

        // ログイン済だったらtrue
        if (session.getAttribute( "userId") != null) {

            int attendanceId = attendanceRepository.findAttendanceIdByUser((int) session.getAttribute("userId"));

            if (attendanceId == 0) {
                boolean isClockingResult = attendanceRepository.clockingIn(place,
                        (int) session.getAttribute("userId"),
                        (String) session.getAttribute("department_code"));

                if (!isClockingResult) {
                    System.out.println("登録エラー");
                    return "redirect:/index";
                }
                return "redirect:/index";
            } else if (attendanceId == -1) {
                System.out.println("DBError");
                return "redirect:/index";
            } else {
//                model.addAttribute("errorMsg", "出勤済みです。");
                System.out.println("状態  : " + "退勤済");
                return "redirect:/index";
            }
        }
        return "login";
    }

    @PostMapping("clockingOut")
    public String clockingOut(HttpSession session, Model model) {

        if (session.getAttribute( "userId") != null) {

            int attendanceId = attendanceRepository.findAttendanceIdByUser((int) session.getAttribute("userId"));

            if (attendanceId != 0) {
                String workingStatus = attendanceRepository.attendanceStatusById(attendanceId);
                if (workingStatus.equals("出勤中")) {
                    boolean isClockingResult = attendanceRepository.clockingOut(attendanceId);
                    if (!isClockingResult) {
                        System.out.println("登録エラー");
                        return "redirect:/index";
                    }
                    return "redirect:/index";
                } else if (workingStatus.equals("休憩中")) {
//                    model.addAttribute("errorMsg", "休憩中です。");
                    System.out.println("状態  : " + "現在休憩中です。休憩を終了してから退勤してください。");
                    return "redirect:/index";
                }
            } else {
//                model.addAttribute("errorMsg", "未出勤です。");
                System.out.println("状態  : " + "未出勤");
                return "redirect:/index";
            }
        }
        return "login";
    }

    @PostMapping("startBreak")
    public String startBreak(HttpSession session, Model model) {

        if (session.getAttribute( "userId") != null) {

            int attendanceId = attendanceRepository.findAttendanceIdByUser((int) session.getAttribute("userId"));

            if (attendanceId != 0) {
                String isWorkingStatus = attendanceRepository.attendanceStatusById(attendanceId);
                if (isWorkingStatus.equals("出勤中")) {
                    boolean isResult = attendanceRepository.startBreak(attendanceId);

                    if (!isResult) {
                        System.out.println("登録エラー");
                        return "redirect:/index";
                    }
                        return "redirect:/index";
                } else if (isWorkingStatus.equals("未出勤")) {

                    return "redirect:/index";
                } else if (isWorkingStatus.equals("休憩中")) {
                    System.out.println("すでに休憩中です。");
                    return "redirect:/index";
                } else {
                    return "redirect:/index";
                }
            } else {
                return "redirect:/index";
            }
        }
        return "login";
    }

    @PostMapping("endBreak")
    public String endBreak(HttpSession session, Model model) {

        if (session.getAttribute( "userId") != null) {

            int attendanceId = attendanceRepository.findAttendanceIdByUser((int) session.getAttribute("userId"));

            if (attendanceId != 0) {
                String isWorkingStatus = attendanceRepository.attendanceStatusById(attendanceId);
                if (isWorkingStatus.equals("休憩中")) {
                    boolean isResult = attendanceRepository.endBreak(attendanceId);
                    if (!isResult) {
                        System.out.println("登録エラー");
                        return "redirect:/index";
                    }
                        return "redirect:/index";
                } else if (isWorkingStatus.equals("出勤中")) {
                    System.out.println("状態  : " + "現在出勤中です。");
                    return "redirect:/index";
                }
            } else {
                return "redirect:/index";
            }
        }
        return "login";
    }

    @GetMapping("/historyTest")
    public String attendanceHistory(HttpSession session, Model model){
        List<Map<String,Object>> attendanceList = attendanceRepository.getAttendanceHistory((Integer) session.getAttribute("useId"));
        //List<Map<String,Object>> attendanceList = attendanceRepository.getAttendanceHistory(2);
        model.addAttribute("attendancelist",attendanceList);
        return "attendance_history";
    }

//    @PostMapping("/tset")
//    public String attendanc(HttpSession session,
//                            @RequestParam("place") String place,
//                            Model model) {
//
//        if (session.getAttribute("userId") != null) {
//            int attendanceId = attendanceRepository.findAttendanceIdByUser((int) session.getAttribute("userId"));
//            if (attendanceId == 0) {
//                boolean isClokingResult = attendanceRepository.clockingIn(place,
//                        (int) session.getAttribute("userId"),
//                        (String) session.getAttribute("department_code"));
//
//            } else {
//                String isWorkingStatus = attendanceRepository.attendanceStatusById(attendanceId);
//                if (isWorkingStatus.equals("出勤中")) {
//                    if () {
//                        boolean isResult = attendanceRepository.startBreak(attendanceId);
//                        return "index";
//                    } else if () {
//                        boolean isResult = attendanceRepository.clockingOut(attendanceId);
//                        return "index";
//                    }
//                } else if (isWorkingStatus.equals("休憩中")) {
//                    boolean isResult = attendanceRepository.endBreak(attendanceId);
//                    return "index";
//                } else {
//                    return "index";
//                }
//            }
//        }
//        return "login";
//    }
}
