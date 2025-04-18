package com.student_management.students.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}
