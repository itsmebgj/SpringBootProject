package com.example.demo.service;

import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean validateUser(String id, String password) {
        return userRepository.findById(id)
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }
}