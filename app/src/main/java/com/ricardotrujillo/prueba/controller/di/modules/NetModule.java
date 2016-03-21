package com.ricardotrujillo.prueba.controller.di.modules;

import com.ricardotrujillo.prueba.controller.BusWorker;
import com.ricardotrujillo.prueba.controller.DbWorker;
import com.ricardotrujillo.prueba.controller.LogWorker;
import com.ricardotrujillo.prueba.controller.NetWorker;
import com.ricardotrujillo.prueba.controller.SharedPreferencesWorker;
import com.ricardotrujillo.prueba.controller.di.scopes.AppScope;

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
