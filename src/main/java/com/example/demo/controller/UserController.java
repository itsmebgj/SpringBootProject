package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpSession; // HttpSession 임포트
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // 모든 뷰에서 'user' 속성을 사용할 수 있도록 설정
    @ModelAttribute
    public void addUserToModel(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam String id, @RequestParam String password, Model model, HttpSession session) {
        boolean isValidUser = userService.validateUser(id, password);
    
        if (isValidUser) {
            User user = userService.findById(id); // UserService에 findById 메서드 추가 필요
            session.setAttribute("user", user);
            model.addAttribute("message", "로그인에 성공하였습니다.");
            model.addAttribute("user", user); // 로그인 성공 시 user 객체도 전달
        } else {
            model.addAttribute("message", "아이디 또는 비밀번호가 잘못되었습니다.");
        }
        return "home"; // 현재 페이지(home)로 리턴
    }
    
    

    @PostMapping("/signup")
    public String signup(@RequestParam String new_user_name, 
                         @RequestParam String new_user_dob,
                         @RequestParam String new_user_id, 
                         @RequestParam String new_user_pw, 
                         Model model) {
        User user = new User();
        user.setId(new_user_id);
        user.setName(new_user_name);
        user.setBirth(new_user_dob);
        user.setPassword(new_user_pw);

        userService.saveUser(user); // 사용자 저장
        model.addAttribute("message", "회원가입 성공! 로그인 해주세요.");
        return "home"; // 회원가입 후 홈 페이지로 이동
    }

    @PostMapping("/find-id")
    public String findId(@RequestParam String find_user_name, 
                         @RequestParam String find_user_dob, 
                         Model model) {
        String userId = userService.findId(find_user_name, find_user_dob);
        
        if (userId != null) {
            model.addAttribute("message", "찾은 아이디: " + userId);
        } else {
            model.addAttribute("message", "해당 정보를 가진 아이디가 없습니다.");
        }
        return "home"; // 결과를 표시할 페이지로 이동
    }

    @PostMapping("/find-password")
    public String findPassword(@RequestParam String new_user_id, 
                               @RequestParam String find_user_name, 
                               @RequestParam String find_user_dob, 
                               Model model) {
        String password = userService.findPassword(new_user_id, find_user_name, find_user_dob);
        
        if (password != null) {
            model.addAttribute("message", "찾은 비밀번호: " + password);
        } else {
            model.addAttribute("message", "해당 정보를 가진 계정이 없습니다.");
        }
        return "home"; // 결과를 표시할 페이지로 이동
    }

    // 로그아웃 엔드포인트
    @GetMapping("/logout")
    public String logout(HttpSession session, Model model) {
        session.invalidate(); // 세션 무효화
        model.addAttribute("message", "로그아웃 되었습니다.");
        return "home"; // 로그아웃 후 홈 페이지로 이동
    }
}
