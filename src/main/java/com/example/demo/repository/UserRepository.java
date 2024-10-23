package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByNameAndBirth(String name, String birth);
    User findByIdAndNameAndBirth(String id, String name, String birth);
}
