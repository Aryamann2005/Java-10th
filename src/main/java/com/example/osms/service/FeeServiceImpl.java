package com.example.osms.service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

import com.example.osms.dao.StudentDAOImpl;
import com.example.osms.model.Payment;
import com.example.osms.model.Student;

public class FeeServiceImpl implements FeeService {

    private final StudentDAOImpl studentDAO;

    public FeeServiceImpl(StudentDAOImpl studentDAO) {
        this.studentDAO = studentDAO;
    }

    @Override
    @Transactional
    public Integer addStudent(Student s) {
        return studentDAO.save(s);
    }

    @Override
    @Transactional
    public void updateStudent(Student s) {
        studentDAO.update(s);
    }

    @Override
    @Transactional
    public void deleteStudent(Integer id) {
        Student s = studentDAO.findById(id);
        if (s != null) {
            studentDAO.delete(s);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Student getStudent(Integer id) {
        return studentDAO.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> getAllStudents() {
        return studentDAO.findAll();
    }

    @Override
    @Transactional
    public void makePayment(Integer studentId, double amount) {
        Student s = studentDAO.findById(studentId);
        if (s == null) {
            throw new RuntimeException("Student not found");
        }
        if (amount <= 0) {
            throw new RuntimeException("Invalid amount");
        }

        // Deduct balance
        s.setBalance(s.getBalance() - amount);
        studentDAO.update(s);

        // Record payment
        var session = studentDAO.getSessionFactory().getCurrentSession();
        Payment p = new Payment(s, amount);
        session.save(p);
    }

    @Override
    @Transactional
    public void refundPayment(Integer studentId, double amount) {
        Student s = studentDAO.findById(studentId);
        if (s == null) {
            throw new RuntimeException("Student not found");
        }
        if (amount <= 0) {
            throw new RuntimeException("Invalid amount");
        }

        // Add balance
        s.setBalance(s.getBalance() + amount);
        studentDAO.update(s);

        // Record refund (negative payment)
        var session = studentDAO.getSessionFactory().getCurrentSession();
        Payment p = new Payment(s, -amount);
        session.save(p);
    }
}
