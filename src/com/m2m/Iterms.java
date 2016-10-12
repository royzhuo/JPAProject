package com.m2m;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

/**
 * @author roy.zhuo
 */
//商品名称
@Entity
public class Iterms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    /*
    * name:中间表的名称
    * joinColumns:@JoinColumn(name当前类在中间表的外健，referencedColumnName中间表的外健直线当前类的主键
    * inverseJoinColumns:@JoinColumn(name另一个类在中间表的外健，referencedColumnName中间表的外健直线另一个类的主键
    *
    * */
    @JoinTable(name = "Iterms_Category",
        joinColumns = {@JoinColumn(name = "Iterm_id", referencedColumnName = "ID")
        },
        inverseJoinColumns = {@JoinColumn(name = "category_id", referencedColumnName = "ID")}
    )
    @ManyToMany
    private Set<Category> categories = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }
}
