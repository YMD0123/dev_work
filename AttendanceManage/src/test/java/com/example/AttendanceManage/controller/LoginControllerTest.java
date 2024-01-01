//package com.example.AttendanceManage.controller;
//
//import com.example.AttendanceManage.Repository.UserRepository;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.ui.Model;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class LoginControllerTest {
//
//    @Mock
//    private UserRepository userRepository;
//    @Mock
//    private Model model;
//    @InjectMocks
//    private LoginController loginController;
//
//    @Test
//    void loginPage() {
//        String result = loginController.loginPage();
//        assertEquals("login", result);
//    }
//
//    @Test
//    void login() {
//        when(userRepository.Login(1, "users")).thenReturn(true);
//        String result = loginController.login(1, "users", model);
//        assertEquals("redirect:/index", result);
//    }
//
//    // ログイン成功時テスト
//    @Test
//    void loginSuccess() {
//        when(userRepository.Login(1, "users")).thenReturn(true);
//        String result = loginController.login(1, "users", model);
//        assertEquals("redirect:/index", result);
//    }
//
//    // ログイン失敗時テスト
//    @Test
//    void loginFailure() {
//        when(userRepository.Login(1, "admin")).thenReturn(false);
//        String result = loginController.login(1, "admin", model);
//        assertEquals("login", result);
////        assertTrue(model.containsAttribute("error"));
//    }
//
//    @Test
//    void indexPage() {
//        String result = loginController.indexPage();
//        assertEquals("index", result);
//    }
//}