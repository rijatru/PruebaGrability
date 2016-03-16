package com.ricardotrujillo.prueba.di.components;

import com.ricardotrujillo.prueba.App;
import com.ricardotrujillo.prueba.activities.EntryActivity;
import com.ricardotrujillo.prueba.activities.MainActivity;
import com.ricardotrujillo.prueba.adapter.StoreRecyclerViewAdapter;
import com.ricardotrujillo.prueba.di.modules.NetModule;
import com.ricardotrujillo.prueba.di.modules.SchoolModule;
import com.ricardotrujillo.prueba.di.scopes.AppScope;
import com.ricardotrujillo.prueba.fragments.StoreFragment;
import com.ricardotrujillo.prueba.model.EntryViewModel;

@AppScope
@dagger.Component(modules = {SchoolModule.class, NetModule.class})
public interface AppComponent {

    void inject(App app);

    void inject(MainActivity activity);

    void inject(EntryActivity entryActivity);

    void inject(StoreFragment fragment);

    void inject(StoreRecyclerViewAdapter storeRecyclerViewAdapter);

    void inject(EntryViewModel entryViewModel);
}
