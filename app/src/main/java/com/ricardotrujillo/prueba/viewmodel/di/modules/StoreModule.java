package com.ricardotrujillo.prueba.viewmodel.di.modules;

import com.ricardotrujillo.prueba.model.StoreManager;
import com.ricardotrujillo.prueba.viewmodel.di.scopes.AppScope;

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
