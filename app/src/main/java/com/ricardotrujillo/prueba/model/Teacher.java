package com.ricardotrujillo.prueba.model;

import javax.inject.Inject;

public class Teacher {

    private String name;
    private String grade;

    @Inject
    public Teacher() {

    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getGrade() {

        return grade;
    }

    public void setGrade(String grade) {

        this.grade = grade;
    }
}
