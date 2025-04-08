package com.student_management.students.dao;

import com.student_management.students.StudentsApplication;
import com.student_management.students.model.Student;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class StudentDao {

    private final JdbcTemplate jdbcTemplate;

    private RowMapper<Student> studentRowMapper = (rs, rowNum) -> {
        Student student = new Student();
        student.setId(rs.getInt("id"));
        student.setName(rs.getString("name"));
        student.setAge(rs.getInt("age"));
        student.setEmail(rs.getString("email"));
        return student;
    };

    public int save(Student student){
        String sql = "INSERT INTO student(name, age, email) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, student.getName(), student.getAge(), student.getEmail());
    }

    public List<Student> getAll(){
        String sql = "SELECT * FROM students";
        return jdbcTemplate.query(sql, studentRowMapper);
    }

    public int deleteById(int id){
        String sql = "DELETE FROM students where id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
