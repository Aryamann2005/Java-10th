package com.example.osms.model;

import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Integer paymentId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id")
    private Student student;

    private double amount;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    public Payment() {}

    public Payment(Student student, double amount) {
        this.student = student;
        this.amount = amount;
        this.paymentDate = LocalDateTime.now();
    }

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", studentId=" + (student != null ? student.getStudentId() : null) +
                ", amount=" + amount +
                ", paymentDate=" + paymentDate +
                '}';
    }
}
