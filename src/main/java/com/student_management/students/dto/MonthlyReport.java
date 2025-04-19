package com.student_management.students.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlyReport {
    private String month;
    private double totalAmount;
}
