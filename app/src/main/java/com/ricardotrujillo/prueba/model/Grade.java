package com.ricardotrujillo.prueba.model;

import android.content.Context;

import java.util.ArrayList;

import javax.inject.Inject;

public class Grade {

    private ArrayList<Teacher> teachers = new ArrayList<>();
    private ArrayList<Student> students = new ArrayList<>();
    private String name;
    private Measurements measurements = new Measurements();

    @Inject
    public Grade() {

    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public int getScreenHeight() {

        return measurements.getScreenHeight();
    }

    public void setScreenHeight(Context context) {

        measurements.setScreenHeight(context);
    }

    public void addTeacher(Teacher teacher) {

        teachers.add(teacher);
    }

    public ArrayList getTeachers() {

        return teachers;
    }

    public void addStudent(Student student) {

        students.add(0, student);
    }

    public ArrayList<Student> getStudents() {

        return students;
    }

    public Student getStudent(int position) {

        return students.get(position);
    }
}
