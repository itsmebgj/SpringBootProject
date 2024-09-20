package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.demo.service.UserService;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public String login(@RequestParam String id, @RequestParam String password, Model model) {
        boolean isValidUser = userService.validateUser(id, password);
        model.addAttribute("message", isValidUser ? "로그인 성공" : "아이디 또는 비밀번호가 잘못되었습니다.");
        return "home";
    }
}
