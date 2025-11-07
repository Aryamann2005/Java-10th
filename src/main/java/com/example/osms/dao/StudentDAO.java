package com.example.osms.dao;

import java.util.List;
import com.example.osms.model.Student;

public interface StudentDAO {
    Integer save(Student s);
    void update(Student s);
    void delete(Student s);
    Student findById(Integer id);
    List<Student> findAll();
}
