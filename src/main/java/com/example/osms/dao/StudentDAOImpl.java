package com.example.osms.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.example.osms.model.Student;

public class StudentDAOImpl implements StudentDAO {

    private final SessionFactory sessionFactory;

    public StudentDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Integer save(Student s) {
        return (Integer) currentSession().save(s);
    }

    @Override
    public void update(Student s) {
        currentSession().update(s);
    }

    @Override
    public void delete(Student s) {
        currentSession().delete(s);
    }

    @Override
    public Student findById(Integer id) {
        return currentSession().get(Student.class, id);
    }

    @Override
    public List<Student> findAll() {
        Query<Student> query = currentSession().createQuery("from Student", Student.class);
        return query.getResultList();
    }
}
