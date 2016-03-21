package com.ricardotrujillo.prueba.viewmodel.activity;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionInflater;

import com.google.gson.Gson;
import com.ricardotrujillo.prueba.App;
import com.ricardotrujillo.prueba.R;
import com.ricardotrujillo.prueba.databinding.ActivityMainBinding;
import com.ricardotrujillo.prueba.model.Store;
import com.ricardotrujillo.prueba.model.StoreManager;
import com.ricardotrujillo.prueba.viewmodel.Constants;
import com.ricardotrujillo.prueba.viewmodel.event.FetchedStoreDataEvent;
import com.ricardotrujillo.prueba.viewmodel.worker.BusWorker;
import com.ricardotrujillo.prueba.viewmodel.worker.DbWorker;
import com.ricardotrujillo.prueba.viewmodel.worker.LogWorker;
import com.ricardotrujillo.prueba.viewmodel.worker.MeasurementsWorker;
import com.ricardotrujillo.prueba.viewmodel.worker.NetWorker;

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
    MeasurementsWorker measurementsWorker;
    @Inject
    BusWorker busWorker;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inject();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initTransition();

        measurementsWorker.setScreenHeight(this);
        measurementsWorker.setScreenOrientation(this);

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

            busWorker.getBus().post(new FetchedStoreDataEvent());
        }
    }

    void getData(String url) {

        netWorker.get(this, url, new NetWorker.Listener() {

            @Override
            public void onDataRetrieved(String result) {

                Store store = new Gson().fromJson(result.replace(Constants.STRING_TO_ERASE, Constants.NEW_STRING), Store.class);

                storeManager.addStore(store);

                dbWorker.saveObject(MainActivity.this, store);

                busWorker.getBus().post(new FetchedStoreDataEvent());
            }
        });
    }

    void inject() {

        ((App) getApplication()).getAppComponent().inject(this);
    }
}
