package com.ricardotrujillo.prueba.di.modules;

import com.ricardotrujillo.prueba.workers.BusWorker;
import com.ricardotrujillo.prueba.workers.DbWorker;
import com.ricardotrujillo.prueba.workers.LogWorker;
import com.ricardotrujillo.prueba.workers.SharedPreferencesWorker;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class NetModule {

    @Provides
    @Singleton
    SharedPreferencesWorker provideSharedPreferences(){

        return new SharedPreferencesWorker();
    }

    @Provides
    @Singleton
    DbWorker provideDbWorker(){

        return new DbWorker();
    }

    @Provides
    @Singleton
    LogWorker provideLogWorker(){

        return new LogWorker();
    }

    @Provides
    @Singleton
    BusWorker provideBusWorker(){

        return new BusWorker();
    }
}
