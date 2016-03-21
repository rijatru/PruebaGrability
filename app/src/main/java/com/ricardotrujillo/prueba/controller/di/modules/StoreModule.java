package com.ricardotrujillo.prueba.controller.di.modules;

import com.ricardotrujillo.prueba.controller.di.scopes.AppScope;
import com.ricardotrujillo.prueba.model.StoreManager;

import dagger.Module;
import dagger.Provides;

@Module
public class StoreModule {

    @Provides
    @AppScope
    StoreManager provideStoreManager() {

        return new StoreManager();
    }
}
