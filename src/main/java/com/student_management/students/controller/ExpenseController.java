package com.student_management.students.controller;

import com.student_management.students.dto.MonthlyReport;
import com.student_management.students.model.Expense;
import com.student_management.students.model.User;
import com.student_management.students.repository.ExpenseRepository;
import com.student_management.students.repository.UserRespository;
import com.student_management.students.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;
    private final UserRespository userRespository;

    @PostMapping
    public ResponseEntity<Expense> addExpense(@RequestBody Expense expense, Authentication authentication){
        User user = userRespository.findsByUsername(authentication.getName())
                .orElseThrow();
        expense.setUser(user);
        return ResponseEntity.ok(expenseService.save(expense));
    }

    @GetMapping
    public ResponseEntity<List<Expense>> getAll(Authentication auth){
        User user = userRespository.findsByUsername(auth.getName()).orElseThrow();
        return ResponseEntity.ok(expenseService.getAllByUser(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        expenseService.deleteById(id);
        return ResponseEntity.ok("Expense deleted");
    }

    @GetMapping("/monthly-report")
    public ResponseEntity<List<MonthlyReport>> getMonthlyReport(@RequestParam int year, Authentication auth){
        User user = userRespository.findsByUsername(auth.getName()).orElseThrow();
        return ResponseEntity.ok(expenseService.getMonthlyReport(user, year));
    }


}
