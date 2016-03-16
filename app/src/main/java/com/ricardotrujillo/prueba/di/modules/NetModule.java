package com.ricardotrujillo.prueba.di.modules;

import com.ricardotrujillo.prueba.di.scopes.AppScope;
import com.ricardotrujillo.prueba.workers.BusWorker;
import com.ricardotrujillo.prueba.workers.DbWorker;
import com.ricardotrujillo.prueba.workers.LogWorker;
import com.ricardotrujillo.prueba.workers.NetWorker;
import com.ricardotrujillo.prueba.workers.SharedPreferencesWorker;

import dagger.Module;
import dagger.Provides;

@Module
public class NetModule {

    @Provides
    @AppScope
    SharedPreferencesWorker provideSharedPreferences(){

        return new SharedPreferencesWorker();
    }

    @Provides
    @AppScope
    DbWorker provideDbWorker(){

        return new DbWorker();
    }

    @Provides
    @AppScope
    LogWorker provideLogWorker(){

        return new LogWorker();
    }

    @Provides
    @AppScope
    BusWorker provideBusWorker(){

        return new BusWorker();
    }

    @Provides
    @AppScope
    NetWorker provideNetWorker() {

        return new NetWorker();
    }
}
