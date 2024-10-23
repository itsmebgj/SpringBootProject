package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean validateUser(String id, String password) {
        return userRepository.findById(id)
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }

    public void saveUser(User user) {
        userRepository.save(user); // 사용자 정보 저장
    }

    public String findId(String name, String dob) {
        User user = userRepository.findByNameAndBirth(name, dob);
        return user != null ? user.getId() : null; // 아이디 반환
    }

    public String findPassword(String id, String name, String dob) {
        User user = userRepository.findByIdAndNameAndBirth(id, name, dob);
        return user != null ? user.getPassword() : null; // 비밀번호 반환
    }

    public User findById(String id) {
        Optional<User> userOpt = userRepository.findById(id);
        return userOpt.orElse(null);
    }
}
