package com.example.osms.service;

import java.util.List;
import com.example.osms.model.Student;

public interface FeeService {
    Integer addStudent(Student s);
    void updateStudent(Student s);
    void deleteStudent(Integer id);
    Student getStudent(Integer id);
    List<Student> getAllStudents();

    // Fee-related operations
    void makePayment(Integer studentId, double amount);
    void refundPayment(Integer studentId, double amount);
}
