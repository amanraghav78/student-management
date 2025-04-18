package com.student_management.students.repository;

import com.student_management.students.model.Expense;
import com.student_management.students.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUser(User user);
}
