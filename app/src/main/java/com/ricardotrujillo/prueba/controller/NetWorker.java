package com.ricardotrujillo.prueba.controller;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ricardotrujillo.prueba.Constants;
import com.ricardotrujillo.prueba.Utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.inject.Inject;

public class NetWorker {

    private Measurements measurements = new Measurements();
    private RequestQueue queue;
    @Inject
    public NetWorker() {

    }

    public static boolean isConnected(Activity activity) {

        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void isNetworkAvailable(Context context, final ConnectionStatusListener listener) {

        new CheckConnectivity(context, new ConnectionStatusListener() {

            @Override
            public void onResult(boolean connected) {

                listener.onResult(connected);
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void get(final Context context, final String url, final Listener listener) {

        queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String res) {

                listener.onDataRetrieved(res);
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("Test", "onErrorResponse " + error.toString());

                get(context, url, listener);
            }
        });

        RetryPolicy policy = new DefaultRetryPolicy(Constants.SOCKET_TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        stringRequest.setRetryPolicy(policy);
        stringRequest.setTag(Constants.TAG);

        queue.add(stringRequest);
    }

    public void cancelAll() {

        if (queue != null) queue.cancelAll(Constants.TAG);
    }

    public int getScreenHeight() {

        return measurements.getScreenHeight();
    }

    public void setScreenHeight(Context context) {

        measurements.setScreenHeight(context);
    }

    public interface ConnectionStatusListener {

        void onResult(boolean connected);
    }

    public interface Listener {

        void onDataRetrieved(String result);
    }

    private class CheckConnectivity extends AsyncTask<String, Boolean, Boolean> {

        Context activity;
        ConnectionStatusListener listener;

        public CheckConnectivity(Context activity, ConnectionStatusListener listener) {

            this.activity = activity;
            this.listener = listener;
        }

        @Override
        protected Boolean doInBackground(String... uri) {

            if (isNetworkAvailable()) {

                try {

                    HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                    urlc.setRequestProperty("User-Agent", "Test");
                    urlc.setRequestProperty("Connection", "close");
                    urlc.setConnectTimeout(3000);
                    urlc.connect();

                    return (urlc.getResponseCode() == 200);

                } catch (IOException e) {

                }

            } else {

                return false;
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean connected) {
            super.onPostExecute(connected);

            listener.onResult(connected);
        }

        private boolean isNetworkAvailable() {

            ConnectivityManager connectivityManager = (ConnectivityManager) activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

            return activeNetworkInfo != null;
        }
    }

    private class Measurements {

        private int rootHeight;

        public int getScreenHeight() {

            return rootHeight;
        }

        public void setScreenHeight(Context context) {

            rootHeight = Utils.getScreenHeight(context);
        }
    }
}
