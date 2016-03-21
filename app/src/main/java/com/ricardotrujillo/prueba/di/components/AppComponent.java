package com.ricardotrujillo.prueba.di.components;

import com.ricardotrujillo.prueba.App;
import com.ricardotrujillo.prueba.adapter.StoreRecyclerViewAdapter;
import com.ricardotrujillo.prueba.di.modules.NetModule;
import com.ricardotrujillo.prueba.di.modules.StoreModule;
import com.ricardotrujillo.prueba.di.scopes.AppScope;
import com.ricardotrujillo.prueba.fragments.StoreFragment;
import com.ricardotrujillo.prueba.model.EntryViewModel;
import com.ricardotrujillo.prueba.view.EntryActivity;
import com.ricardotrujillo.prueba.view.MainActivity;

@AppScope
@dagger.Component(modules = {StoreModule.class, NetModule.class})
public interface AppComponent {

    void inject(App app);

    void inject(MainActivity activity);

    void inject(EntryActivity entryActivity);

    void inject(StoreFragment fragment);

    void inject(StoreRecyclerViewAdapter storeRecyclerViewAdapter);

    void inject(EntryViewModel entryViewModel);
}
