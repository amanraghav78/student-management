package com.student_management.students.controller;

import com.student_management.students.model.Student;
import com.student_management.students.service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;

@RestController
@RequestMapping("/students")
@AllArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<String> saveStudent(@RequestBody Student student){
        studentService.save(student);
        return ResponseEntity.ok("Student added successfully");
    }

    @GetMapping
    public List<Student> getAllStudent(){
        return studentService.getAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable int id){
        studentService.deleteById(id);
        return ResponseEntity.ok("Student deleted successfully");
    }
}
