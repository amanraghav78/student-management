package com.student_management.students.repository;

import com.student_management.students.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRespository extends JpaRepository<User,Long> {
    Optional<User> findsByUsername(String username);
}
