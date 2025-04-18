package com.student_management.students.service;

import com.student_management.students.model.Expense;
import com.student_management.students.model.User;
import com.student_management.students.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public Expense save(Expense expense){
        return expenseRepository.save(expense);
    }

    public List<Expense> getAllByUser(User user){
        return expenseRepository.findByUser(user);
    }

    public void deleteById(Long id){
        expenseRepository.deleteById(id);
    }
}
