package com.ricardotrujillo.prueba;

import android.app.Application;
import android.util.Log;

import com.ricardotrujillo.prueba.di.components.AppComponent;
import com.ricardotrujillo.prueba.di.components.DaggerAppComponent;
import com.ricardotrujillo.prueba.model.MessageEvent;
import com.ricardotrujillo.prueba.workers.BusWorker;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

/**
 * Created by Ricardo on 15/03/2016.
 */
public class App extends Application {

    @Inject
    BusWorker busWorker;

    private AppComponent appComponent;

    @Inject
    public App() {

    }

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.create();
        appComponent.inject(this);

        busWorker.register(this);
    }

    @Subscribe
    public void recievedMessage(MessageEvent event) {

        Log.d("Dagger", "recievedMessage App: " + event.getMessage());
    }

    public AppComponent getAppComponent() {

        return appComponent;
    }
}