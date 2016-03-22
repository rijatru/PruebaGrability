package com.ricardotrujillo.prueba;

import android.app.Application;

import com.google.gson.Gson;
import com.ricardotrujillo.prueba.model.Store;
import com.ricardotrujillo.prueba.model.StoreManager;
import com.ricardotrujillo.prueba.viewmodel.Constants;
import com.ricardotrujillo.prueba.viewmodel.di.components.AppComponent;
import com.ricardotrujillo.prueba.viewmodel.di.components.DaggerAppComponent;
import com.ricardotrujillo.prueba.viewmodel.event.FetchedStoreDataEvent;
import com.ricardotrujillo.prueba.viewmodel.event.MessageEvent;
import com.ricardotrujillo.prueba.viewmodel.event.RequestStoreEvent;
import com.ricardotrujillo.prueba.viewmodel.worker.BusWorker;
import com.ricardotrujillo.prueba.viewmodel.worker.DbWorker;
import com.ricardotrujillo.prueba.viewmodel.worker.NetWorker;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

/**
 * Created by Ricardo on 15/03/2016.
 */
public class App extends Application {

    @Inject
    NetWorker netWorker;
    @Inject
    DbWorker dbWorker;
    @Inject
    StoreManager storeManager;
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

        checkForLoadedData(0);
    }

    @Subscribe
    public void recievedMessage(MessageEvent event) {

    }

    @Subscribe
    public void recievedMessage(RequestStoreEvent event) {

        checkForLoadedData(event.getPosition());
    }

    void checkForLoadedData(int position) {

        if (storeManager.getStore() == null) {

            checkForConnectivity(position);

        } else {

            busWorker.getBus().post(new FetchedStoreDataEvent(position));
        }
    }

    void checkForConnectivity(final int position) {

        netWorker.isNetworkAvailable(this, new NetWorker.ConnectionStatusListener() {

            @Override
            public void onResult(boolean connected) {

                if (connected) {

                    getData(position, Constants.URL);

                } else {

                    getSavedData(position);
                }
            }
        });
    }

    void getSavedData(int position) {

        Store store = (Store) dbWorker.getObject(this);

        if (store != null) {

            storeManager.addStore(store);

            busWorker.getBus().post(new FetchedStoreDataEvent(position));
        }
    }

    void getData(final int position, String url) {

        netWorker.get(this, url, new NetWorker.Listener() {

            @Override
            public void onDataRetrieved(String result) {

                Store store = new Gson().fromJson(result.replace(Constants.STRING_TO_ERASE, Constants.NEW_STRING), Store.class);

                store.feed.fillOriginalEntry(store.feed.entry);

                storeManager.addStore(store);

                dbWorker.saveObject(getApplicationContext(), store);

                busWorker.getBus().post(new FetchedStoreDataEvent(position));
            }
        });
    }

    public AppComponent getAppComponent() {

        return appComponent;
    }
}