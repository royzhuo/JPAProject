package com.Spring;

import com.domain.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author roy.zhuo
 */
@Service
public class StudentService {

    @Autowired
    private StudentDao studentDao;

    @Transactional
    public void addStudent(Student student, Student student2) {
        studentDao.addStudent(student);
        int i = 10 / 0;
        studentDao.addStudent(student2);

    }
}
