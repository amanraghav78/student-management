package com.student_management.students.service;

import com.student_management.students.dao.StudentDao;
import com.student_management.students.model.Student;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StudentService {

    private final StudentDao studentDao;

    public int save(Student student){
        return studentDao.save(student);
    }

    public List<Student> getAll(){
        return studentDao.getAll();
    }

    public int deleteById(int id){
        return studentDao.deleteById(id);
    }
}
