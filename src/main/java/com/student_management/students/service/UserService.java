package com.student_management.students.service;

import com.student_management.students.model.User;
import com.student_management.students.repository.UserRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRespository userRespository;
    private final PasswordEncoder passwordEncoder;

    public User saveUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRespository.save(user);
    }

    public Optional<User> findByUsername(String username){
        return userRespository.findsByUsername(username);
    }
}
