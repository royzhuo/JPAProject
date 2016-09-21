package com.test;

import com.domain.Student;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Date;

/**
 * @author roy.zhuo
 */
public class TestJpa {

    public static void main(String[] args) {
        //创建entitymanageFactory
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("JPAProject");
        //创建entitymanage 相当于hibernate session
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        //得到和开启事物
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        //进行持久化操作
        Student student = new Student();
        student.setName("roy");
        student.setAddress("xiamen");
        student.setBirthday(new Date());
        student.setCreateTime(new Date());
        entityManager.persist(student);

        //事物提交
        entityTransaction.commit();
        //关闭manage
        entityManager.close();
        entityManagerFactory.close();
    }
}
