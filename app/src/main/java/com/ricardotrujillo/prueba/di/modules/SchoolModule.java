package com.ricardotrujillo.prueba.di.modules;

import com.ricardotrujillo.prueba.di.scopes.AppScope;
import com.ricardotrujillo.prueba.model.StoreManager;

import dagger.Module;
import dagger.Provides;

@Module
public class SchoolModule {

    @Provides
    @AppScope
    StoreManager provideStore() {

        return new StoreManager();
    }
}
