package com.ricardotrujillo.prueba.workers;

import android.util.Log;
import com.ricardotrujillo.prueba.Constants;

import javax.inject.Inject;

public class LogWorker {

    @Inject
    public LogWorker() {

    }

    public void log(String message) {

        Log.d(Constants.LogTag, message);
    }
}
