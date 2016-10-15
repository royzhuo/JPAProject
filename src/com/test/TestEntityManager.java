package com.test;

import com.domain.Student;
import com.m2m.Category;
import com.m2m.Iterms;
import com.m2o.Customer;
import com.m2o.Order;
import com.o2m.Dept;
import com.o2m.Employee;
import com.o2o.School;
import com.o2o.SchoolBoss;
import org.hibernate.ejb.QueryHints;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        student.setAddress("77788877");
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


    //关联关系

    //1.单向多对一
    @Test
    public void testM2o() {
        Customer customer = new Customer();
        customer.setName("roy");
        customer.setMoney(200);

        Order order1 = new Order();
        order1.setName("iphone");
        order1.setPrice(10);

        Order order2 = new Order();
        order2.setName("mac");
        order2.setPrice(20);

        //配置关联关系
        order1.setCustomer(customer);
        order2.setCustomer(customer);

        entityManager.persist(customer);
        entityManager.persist(order1);
        entityManager.persist(order2);

    }

    //默认左外链接，获取多的一方
    @Test
    public void testM2OFind() {
        Order order = entityManager.find(Order.class, 2);
        System.out.println("order:" + order.getName());
        System.out.println("customer:" + order.getCustomer().getName());
    }

    @Test
    public void testM2ORemove() {
        Order order = entityManager.find(Order.class, 1);
        entityManager.remove(order);
    }

    @Test
    public void testM2OUpdate() {
        Order order = entityManager.find(Order.class, 2);
        order.setName("sssss");
    }

    @Test
    public void testO2M() {
        Employee employee = new Employee();
        employee.setName("nick");
        Employee employee2 = new Employee();
        employee2.setName("leo");

        Dept dept = new Dept();
       /* try {
            String name = new String(("安全").getBytes("ISO-8859-1"), "UTF-8");
            dept.setName(name);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
        dept.setName("secureti");

        Set<Employee> employees = new HashSet<>();
        employees.add(employee);
        employees.add(employee2);

        dept.setEmployees(employees);

        entityManager.persist(employee);
        entityManager.persist(employee2);
        entityManager.persist(dept);

    }

    @Test
    public void testO2MFind() {
        Dept dept = entityManager.find(Dept.class, 1);
        System.out.println(dept.getName());
        System.out.println(dept.getEmployees().iterator().next().getName());

    }

    //默认情况下，删除只是将多的一方外健滞空，删除一的一方
    //可以通过设置级联关系来进行级联删除
    @Test
    public void testO2MRemove() {

        Dept dept = entityManager.find(Dept.class, 2);
        entityManager.remove(dept);

    }

    @Test
    public void testO2MUpdate() {
        Dept dept = entityManager.find(Dept.class, 1);

        dept.getEmployees().iterator().next().setName("roy");

    }

    //one to one

    @Test
    public void testOneToOne() {
        School school = new School();
        school.setName("number ten school");
        SchoolBoss schoolBoss = new SchoolBoss();
        schoolBoss.setName("wuya");

        school.setSchoolBoss(schoolBoss);
        schoolBoss.setSchool(school);

        entityManager.persist(schoolBoss);
        entityManager.persist(school);
    }

    @Test
    public void testOnetoOneFind() {
        School school = entityManager.find(School.class, 1);
        System.out.println("school name:" + school.getName());
        System.out.println("--------------------");
        System.out.println("sb:" + school.getSchoolBoss().getClass().getName());
    }

    //多对多
    @Test
    public void testManayToMany() {
        Iterms iterms = new Iterms();
        iterms.setName("apple");
        Iterms iterms1 = new Iterms();
        iterms1.setName("orange");
        Category category = new Category();
        category.setName("food");
        Category category1 = new Category();
        category1.setName("fruit");

        //设置关联关系
        iterms.getCategories().add(category);
        iterms1.getCategories().add(category1);
        category.getItermses().add(iterms);
        category1.getItermses().add(iterms1);

        //保存
        entityManager.persist(iterms);
        entityManager.persist(iterms1);
        entityManager.persist(category);
        entityManager.persist(category1);
    }

    //默认是懒加载
    @Test
    public void testM2MFind() {
        Iterms iterms = entityManager.find(Iterms.class, 1);
        System.out.println("name:" + iterms.getName());
        System.out.println("size:" + iterms.getCategories().size());
    }

    //测试二级缓存
    @Test
    public void testSecondLevelCache() {
        //一级缓存
//        Student student = entityManager.find(Student.class, 1);
//        student = entityManager.find(Student.class, 1);
        //二级缓存
        Student student = entityManager.find(Student.class, 1);
        transaction.commit();
        entityManager.close();


        entityManager = entityManagerFactory.createEntityManager();
        transaction = entityManager.getTransaction();
        transaction.begin();
        Student student1 = entityManager.find(Student.class, 1);
    }

    @Test
    public void testJpqlHelloWorld() {
        //占位符由一开始
        Query query = entityManager.createQuery("from Student where id>=?").setParameter(1, 2);
        int firstResult = query.getFirstResult();
        List<Student> students = query.getResultList();
        System.out.println("firstResult:" + firstResult);
        System.out.println("students:" + students);
    }

    @Test
    public void testQueryCacher() {
        //使用jpql查询缓存,前提配置文件中要配置查询缓存
        String jpa1 = "from Student where id>=?";
        Query query = entityManager.createQuery(jpa1).setHint(QueryHints.HINT_CACHEABLE, true);
        query.setParameter(1, 2);
        int firstResult = query.getFirstResult();
        List<Student> students = query.getResultList();

        query = entityManager.createQuery(jpa1).setHint(QueryHints.HINT_CACHEABLE, true);
        query.setParameter(1, 2);
        firstResult = query.getFirstResult();
        students = query.getResultList();

    }

    @Test
    public void testJpqlOrderBy() {
        String jpa1 = "from Student where id>=? order by id desc ";
        Query query = entityManager.createQuery(jpa1).setHint(QueryHints.HINT_CACHEABLE, true);
        query.setParameter(1, 2);
        List<Student> students = query.getResultList();
        System.out.println(students);
    }

    @Test
    public void testJpqlGruopBy() {
        String sql = "select id from  Student group by name having name='Roy' ";
        Query query = entityManager.createQuery(sql).setHint(QueryHints.HINT_CACHEABLE, true);
        List<Integer> students = query.getResultList();
        System.out.println(students);

    }


}
