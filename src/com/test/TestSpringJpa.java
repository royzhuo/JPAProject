package com.test;

import com.Spring.StudentService;
import com.domain.Student;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;
import java.util.Date;

/**
 * @author roy.zhuo
 */
public class TestSpringJpa {

    private ApplicationContext applicationContext;
    private DataSource dataSource;
    private StudentService studentService;


    {
        applicationContext = new ClassPathXmlApplicationContext("SpringJpa.xml");
        studentService = (StudentService) applicationContext.getBean("studentService");
    }

    @Test
    public void testCon() {
        dataSource = applicationContext.getBean(DataSource.class);
        System.out.println(dataSource.getClass());
    }

    @Test
    public void testAddStudent() {
        Student student = new Student();
        student.setName("roy");
        student.setAddress("xiamen");
        student.setBirthday(new Date());
        student.setCreateTime(new Date());
        Student student1 = new Student();
        student1.setName("rowwwy");
        student1.setAddress("xiawwwwwmen");
        student1.setBirthday(new Date());
        student1.setCreateTime(new Date());
        studentService.addStudent(student, student1);
    }


}
