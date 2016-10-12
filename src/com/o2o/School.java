package com.o2o;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * @author roy.zhuo
 */
@Entity
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @JoinColumn(name = "sb_id", unique = true)
    @OneToOne(fetch = FetchType.LAZY)
    private SchoolBoss schoolBoss;

    public SchoolBoss getSchoolBoss() {
        return schoolBoss;
    }

    public void setSchoolBoss(SchoolBoss schoolBoss) {
        this.schoolBoss = schoolBoss;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
