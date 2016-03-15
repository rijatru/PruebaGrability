package com.ricardotrujillo.prueba.workers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ricardotrujillo.prueba.Constants;

import javax.inject.Inject;

/**
 * Created by Ricardo on 29/02/2016.
 */
public class SharedPreferencesWorker {

    SharedPreferences sharedPreferences;

    @Inject
    public SharedPreferencesWorker() {

    }

    public void saveString(Activity activity, int key, String value) {

        if (sharedPreferences == null) {

            Log.d("Dagger", "saveString Initing shared prefs");

            sharedPreferences = activity.getSharedPreferences(Constants.PREFERENCES_KEY, Context.MODE_PRIVATE);
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(activity.getString(key), value);
        editor.apply();
    }

    public String getString(Activity activity, int key) {

        if (sharedPreferences == null) {

            Log.d("Dagger", "getStringu* Initing shared prefs");

            sharedPreferences = activity.getSharedPreferences(Constants.PREFERENCES_KEY, Context.MODE_PRIVATE);
        }

        return sharedPreferences.getString(activity.getString(key), "");
    }
}
