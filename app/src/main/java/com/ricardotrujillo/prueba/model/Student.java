package com.ricardotrujillo.prueba.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import com.ricardotrujillo.prueba.BR;

import javax.inject.Inject;

/**
 * Created by Ricardo on 29/02/2016.
 */
public class Student extends BaseObservable {

    private String name = "";
    private String grade = "";
    private int likes = 0;
    private boolean isLiked = false;

    @Inject
    public Student() {

    }

    public boolean getIsLiked() {

        return isLiked;
    }

    @Bindable
    public void setIsLiked(boolean isLiked) {

        this.isLiked = isLiked;

        notifyPropertyChanged(BR.name);
    }

    public int getLikes() {

        return likes;
    }

    @Bindable
    public void setLikes(int likes) {

        this.likes = likes;

        notifyPropertyChanged(BR.name);
    }

    public String getName() {

        return name;
    }

    @Bindable
    public void setName(String name) {

        this.name = name;

        notifyPropertyChanged(BR.name);
    }

    public String getGrade() {

        return grade;
    }

    @Bindable
    public void setGrade(String grade) {

        this.grade = grade;

        notifyPropertyChanged(BR.name);
    }

    public void reset() {

        name = "";
        name = "";
    }
}
