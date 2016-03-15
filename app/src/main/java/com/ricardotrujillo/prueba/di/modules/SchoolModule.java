package com.ricardotrujillo.prueba.di.modules;

import com.ricardotrujillo.prueba.di.scopes.AppScope;
import com.ricardotrujillo.prueba.model.Grade;
import com.ricardotrujillo.prueba.model.School;
import com.ricardotrujillo.prueba.model.Student;
import com.ricardotrujillo.prueba.model.Teacher;
import com.ricardotrujillo.prueba.workers.NetWorker;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SchoolModule {

    @Provides
    @AppScope
    School provideSchool() {

        return new School();
    }

    @Provides
    @Singleton
    Grade provideGrade() {

        return new Grade();
    }

    @Provides
    @Singleton
    Teacher provideTeacher() {

        return new Teacher();
    }

    @Provides
    @Singleton
    Student provideStudent() {

        return new Student();
    }

    @Provides
    @Singleton
    NetWorker provideNetWorker() {

        return new NetWorker();
    }
}
