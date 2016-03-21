package com.ricardotrujillo.prueba.viewmodel.worker;

import android.util.Log;

import com.ricardotrujillo.prueba.viewmodel.Constants;

import javax.inject.Inject;

public class LogWorker {

    @Inject
    public LogWorker() {

    }

    public void log(String message) {

        Log.d(Constants.LogTag, message);
    }
}
