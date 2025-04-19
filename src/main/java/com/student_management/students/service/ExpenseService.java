package com.student_management.students.service;

import com.student_management.students.dto.MonthlyReport;
import com.student_management.students.dto.SuggestionDTO;
import com.student_management.students.model.Category;
import com.student_management.students.model.Expense;
import com.student_management.students.model.User;
import com.student_management.students.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public List<MonthlyReport> getMonthlyReport(User user, int year){
        return expenseRepository.getMonthlyReportByYear(user, year);
    }

    public List<SuggestionDTO> getAISuggestions(User user){
        List<SuggestionDTO> suggestions = new ArrayList<>();

        YearMonth current = YearMonth.now();
        YearMonth previous = current.minusMonths(1);

        Map<Category, Double> thisMonth = getCategoryTotals(user, current);
        Map<Category, Double> lastMonth = getCategoryTotals(user, previous);

        for(Category cat : thisMonth.keySet()) {
            double currentAmount = thisMonth.getOrDefault(cat, 0.0);
            double lastAmount = lastMonth.getOrDefault(cat, 0.0);

            if (lastAmount > 0 && currentAmount > lastAmount * 1.3) {
                suggestions.add(new SuggestionDTO(
                        "⚠️ You spent " + String.format("%.0f", (currentAmount / lastAmount - 1) * 100)
                                + "% more on " + cat + " this month than last."));

                double dailyAvg = getDailyAverage(user, current);
                suggestions.add(new SuggestionDTO("Your average daily spending this month  is ₹\" + String.format(\"%.2f\", dailyAvg"));

            }
        }
            return suggestions;

    }

    private Map<Category, Double> getCategoryTotals(User user, YearMonth month){
        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();

        List<Expense> expenses = expenseRepository.findByUserAndDateBetween(user, start, end);

        return  expenses.stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.summingDouble(Expense::getAmount)
                ));
    }

    private double getDailyAverage(User user, YearMonth month){
        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();
        List<Expense> expenses = expenseRepository.findByUserAndDateBetween(user, start, end);

        double total = expenses.stream().mapToDouble(Expense::getAmount).sum();
        return total / ChronoUnit.DAYS.between(start, end.plusDays(1));
    }
}
