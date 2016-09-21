package com.test;

import com.domain.Student;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Date;

/**
 * @author roy.zhuo
 */
public class TestEntityManager {

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction transaction;

    @Before
    public void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("JPAProject");
        entityManager = entityManagerFactory.createEntityManager();
        transaction = entityManager.getTransaction();
        transaction.begin();
    }

    @After
    public void destory() {
        transaction.commit();
        entityManager.close();
        entityManagerFactory.close();
    }

    /*
    * find相当于hibernate get方法，
    * */
    @Test
    public void testFind() {
        Student student = entityManager.find(Student.class, 1);
        System.out.println("----------------------");
        System.out.println(student);
    }

    /*
    * getReference相当于hibernate的load方法,当进行查询时返回的时一个代理对象，只有真正需要的时候才会去调用sql
    * 产生了与hibernate一样的懒加载问题，当entitymanage关闭的时候，就会出现异常
    * */
    @Test
    public void testGetRefrends() {
        Student student = entityManager.getReference(Student.class, 1);
        System.out.println(student.getClass().getName());
        System.out.println("----------------------");
        System.out.println(student);
    }

    /*
    * 相当于hibernate save，但是有点不同的是不能在持久化对象的时候，保存我们设置主键的id,否则会报错
    *
    * */
    @Test
    public void testAdd() {
        Student student = new Student();
        student.setName("Roy2");
        student.setAddress("usa");
        student.setCreateTime(new Date());
        student.setBirthday(new Date());
        entityManager.persist(student);
    }

    /*
    * 只能删除持久化对象，不能删除临时对象。hibernate可以
    *
    * */
    @Test
    public void testDelete() {
        Student student = entityManager.getReference(Student.class, 2);
        entityManager.remove(student);
    }

    /*
    * 当对象是临时对象时，会把临时对象的属性拷贝到meger后的对象，在insert
    * */
    @Test
    public void testMeger1() {
        Student student = new Student();
        student.setName("Roy");
        student.setAddress("usa");
        student.setCreateTime(new Date());
        student.setBirthday(new Date());

        Student student2 = entityManager.merge(student);
        System.out.println("student1#id:" + student.getId());
        System.out.println("student2#id:" + student2.getId());
    }

    /*
   * 当对象是游离对象时，即是emp缓存有该oid的
   * entitymanager缓存池中没有该对象
   * 数据库中没有id为多少的对象
   * 会对游离对象信息进行拷贝，然后insert
   * */
    @Test
    public void testMeger2() {
        Student student = new Student();
        student.setName("Roy");
        student.setAddress("usa");
        student.setCreateTime(new Date());
        student.setBirthday(new Date());
        student.setId(8);

        Student student2 = entityManager.merge(student);
        System.out.println("student1#id:" + student.getId());
        System.out.println("student2#id:" + student2.getId());
    }

    /*
  * 当对象是游离对象时，即是emp缓存有该oid的
  * entitymanager缓存池中没有该对象
  * 数据库中有id为多少的对象
  * 会把该对象从数据库取出来，把游离对象属性拷贝过去，执行update
  * */
    @Test
    public void testMeger3() {
        Student student = new Student();
        student.setName("Roy");
        student.setAddress("7777777");
        student.setCreateTime(new Date());
        student.setBirthday(new Date());
        student.setId(7);

        Student student2 = entityManager.merge(student);

        System.out.println("student2#id:" + student2.getId());
    }

    /*
* 当对象是游离对象时，即是emp缓存有该oid的
* entitymanager缓存池中有该对象
* 会把该对象从缓存池取出来，把游离对象属性拷贝过去，执行update
* */
    @Test
    public void testMeger4() {
        Student student = new Student();
        student.setName("Roy");
        student.setAddress("sssss");
        student.setCreateTime(new Date());
        student.setBirthday(new Date());
        student.setId(7);

        // entityManager.find(Student.class, 7);

        Student student2 = entityManager.find(Student.class, 7);
        entityManager.merge(student);

        System.out.println(student2 == student);//false
    }

    /*
    * 同hibernate flus一样，会在事物提交前发出sql但不会提交事物
    * */
    @Test
    public void testFlu() {
        Student student = entityManager.find(Student.class, 3);
        //entityManager.merge(student);
        System.out.println(student);
        student.setName(new String("阿兰"));
        entityManager.flush();
    }

    /*也同hibernate refresh一样，会将缓存池中对象与数据同同步更新*/
    @Test
    public void testRefresh() {
        Student student = entityManager.find(Student.class, 3);
        student = entityManager.find(Student.class, 3);
        entityManager.refresh(student);
    }

}
