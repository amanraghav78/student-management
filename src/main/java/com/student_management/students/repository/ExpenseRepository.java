package com.student_management.students.repository;

import com.student_management.students.dto.MonthlyReport;
import com.student_management.students.model.Expense;
import com.student_management.students.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUser(User user);

    @Query("SELECT new com.example.expensetracker.expense.MonthlyReport(FUNCTION('TO_CHAR', e.date, 'Month'), SUM(e.amount)) " +
            "FROM Expense e WHERE e.user = :user AND FUNCTION('YEAR', e.date) = :year " +
            "GROUP BY FUNCTION('TO_CHAR', e.date, 'Month'), FUNCTION('MONTH', e.date) " +
            "ORDER BY FUNCTION('MONTH', e.date)")
    List<MonthlyReport> getMonthlyReportByYear(@Param("user") User user, @Param("year") int year);

    List<Expense> findByUserAndDateBetween(User user, LocalDate start, LocalDate end);

}
