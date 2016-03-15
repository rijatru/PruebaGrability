package com.ricardotrujillo.prueba;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.ricardotrujillo.prueba.model.Store;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inject();

        getData(Constants.URL);
    }

    void getData(String url) {

        netWorker.get(this, url, new NetWorker.Listener() {
            @Override
            public void onDataRetrieved(String result) {

                Store storeApp = new Gson().fromJson(result.replace(Constants.STRING_TO_ERASE, Constants.NEW_STRING), Store.class);

                //for (Store.Feed.Entry entry : storeApp.feed.entry) {
//
                //    logWorker.log("label: " + entry.name.label);
                //    logWorker.log("image: " + entry.image[0].label);
                //    logWorker.log("entry: " + entry.name.label);
                //    logWorker.log("entry: " + entry.name.label);
                //    logWorker.log("entry: " + entry.name.label);
                //}

                logWorker.log("Network State: " + netWorker.isNetworkAvailable(MainActivity.this));

                dbWorker.saveObject(MainActivity.this, storeApp);
            }
        });
    }

    void inject() {

        ((App) getApplication()).getAppComponent().inject(this);
    }
}
