package com.ricardotrujillo.prueba;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.ricardotrujillo.prueba.model.FetchedStoreDataEvent;
import com.ricardotrujillo.prueba.model.Store;
import com.ricardotrujillo.prueba.model.StoreManager;
import com.ricardotrujillo.prueba.workers.BusWorker;
import com.ricardotrujillo.prueba.workers.DbWorker;
import com.ricardotrujillo.prueba.workers.LogWorker;
import com.ricardotrujillo.prueba.workers.NetWorker;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    @Inject
    NetWorker netWorker;
    @Inject
    LogWorker logWorker;
    @Inject
    DbWorker dbWorker;
    @Inject
    StoreManager storeManager;
    @Inject
    BusWorker busWorker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inject();

        checkForConnectivity();
    }

    void checkForConnectivity() {

        netWorker.isNetworkAvailable(this, new NetWorker.ConnectionStatusListener() {

            @Override
            public void onResult(boolean connected) {

                logWorker.log("Network State: " + connected);

                if (connected) {

                    getData(Constants.URL);

                } else {

                    getSavedData();
                }
            }
        });
    }

    void getSavedData() {

        Store store = (Store) dbWorker.getObject(this);

        if (store != null) {

            storeManager.addStore(store);

            logWorker.log("Saved Store: " + storeManager.getStore().feed.author.name.label);
        }
    }

    void getData(String url) {

        netWorker.get(this, url, new NetWorker.Listener() {
            @Override
            public void onDataRetrieved(String result) {

                Store store = new Gson().fromJson(result.replace(Constants.STRING_TO_ERASE, Constants.NEW_STRING), Store.class);

                logWorker.log("Got Store: " + result.replace(Constants.STRING_TO_ERASE, Constants.NEW_STRING));

                for (Store.Feed.Entry entry : store.feed.entry) {

                    //logWorker.log("label: " + entry.name.label);
                    //logWorker.log("image: " + entry.image[0].label);
                }

                storeManager.addStore(store);

                logWorker.log("Got Store: " + storeManager.getStore().feed.author.name.label);

                dbWorker.saveObject(MainActivity.this, store);

                busWorker.getBus().post(new FetchedStoreDataEvent());
            }
        });
    }

    void inject() {

        ((App) getApplication()).getAppComponent().inject(this);
    }
}
