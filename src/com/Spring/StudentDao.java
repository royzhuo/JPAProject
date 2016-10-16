package com.Spring;

import com.domain.Student;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author roy.zhuo
 */
@Repository
public class StudentDao {

    @PersistenceContext
    private EntityManager entityManager;


    public void addStudent(Student student) {
        entityManager.persist(student);
    }
}
