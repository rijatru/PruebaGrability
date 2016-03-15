package com.ricardotrujillo.prueba.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.ricardotrujillo.prueba.BR;

import java.util.ArrayList;

import javax.inject.Inject;

public class School extends BaseObservable {

    ArrayList<Grade> grades = new ArrayList<>();
    private String name = "Init name";

    @Inject
    public School() {

        notifyPropertyChanged(BR.name);
    }

    public void addGrade(Grade grade) {

        grades.add(grade);
    }

    @Bindable
    public String getName() {

        return this.name;
    }

    public void setName(String name) {

        this.name = name;

        notifyPropertyChanged(BR.name);
    }
}
