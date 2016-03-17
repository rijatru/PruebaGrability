package com.ricardotrujillo.prueba.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionInflater;

import com.google.gson.Gson;
import com.ricardotrujillo.prueba.App;
import com.ricardotrujillo.prueba.Constants;
import com.ricardotrujillo.prueba.R;
import com.ricardotrujillo.prueba.event.FetchedStoreDataEvent;
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

        initTransition();

        netWorker.setScreenHeight(this);

        checkForLoadedData();
    }

    void initTransition() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            getWindow().setSharedElementExitTransition(TransitionInflater.from(this).inflateTransition(R.transition.entry_transition));
        }
    }

    void checkForLoadedData() {

        if (storeManager.getStore() == null) {

            checkForConnectivity();

        } else {

            logWorker.log("recycling data");

            busWorker.getBus().post(new FetchedStoreDataEvent());
        }
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

            busWorker.getBus().post(new FetchedStoreDataEvent());
        }
    }

    void getData(String url) {

        netWorker.get(this, url, new NetWorker.Listener() {

            @Override
            public void onDataRetrieved(String result) {

                logWorker.log("onDataRetrieved 1");

                Store store = new Gson().fromJson(result.replace(Constants.STRING_TO_ERASE, Constants.NEW_STRING), Store.class);

                logWorker.log("onDataRetrieved 2");

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
