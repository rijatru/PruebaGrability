package com.ricardotrujillo.prueba.di.components;

import com.ricardotrujillo.prueba.App;
import com.ricardotrujillo.prueba.MainActivity;
import com.ricardotrujillo.prueba.di.modules.NetModule;
import com.ricardotrujillo.prueba.di.modules.SchoolModule;
import com.ricardotrujillo.prueba.di.scopes.AppScope;

@AppScope
@dagger.Component(modules = {SchoolModule.class, NetModule.class})
public interface AppComponent {

    void inject(App app);

    void inject(MainActivity activity);
}
