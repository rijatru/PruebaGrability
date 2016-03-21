package com.ricardotrujillo.prueba.viewmodel.di.components;

import com.ricardotrujillo.prueba.App;
import com.ricardotrujillo.prueba.model.EntryViewModel;
import com.ricardotrujillo.prueba.view.adapter.StoreRecyclerViewAdapter;
import com.ricardotrujillo.prueba.viewmodel.activity.EntryActivity;
import com.ricardotrujillo.prueba.viewmodel.activity.MainActivity;
import com.ricardotrujillo.prueba.viewmodel.di.modules.NetModule;
import com.ricardotrujillo.prueba.viewmodel.di.modules.StoreModule;
import com.ricardotrujillo.prueba.viewmodel.di.scopes.AppScope;
import com.ricardotrujillo.prueba.viewmodel.fragment.StoreFragment;

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
