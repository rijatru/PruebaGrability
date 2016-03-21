package com.ricardotrujillo.prueba.controller.di.components;

import com.ricardotrujillo.prueba.App;
import com.ricardotrujillo.prueba.controller.di.modules.NetModule;
import com.ricardotrujillo.prueba.controller.di.modules.StoreModule;
import com.ricardotrujillo.prueba.controller.di.scopes.AppScope;
import com.ricardotrujillo.prueba.model.EntryViewModel;
import com.ricardotrujillo.prueba.view.activity.EntryActivity;
import com.ricardotrujillo.prueba.view.activity.MainActivity;
import com.ricardotrujillo.prueba.view.adapter.StoreRecyclerViewAdapter;
import com.ricardotrujillo.prueba.view.fragment.StoreFragment;

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
