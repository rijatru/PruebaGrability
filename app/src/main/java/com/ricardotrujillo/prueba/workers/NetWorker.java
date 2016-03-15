package com.ricardotrujillo.prueba.workers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.ricardotrujillo.prueba.model.Measurements;

import javax.inject.Inject;

public class NetWorker {

    private Measurements measurements = new Measurements();
    private RequestQueue queue;

    @Inject
    public NetWorker() {

    }

    public boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void get(final Context context, final String url, final Listener listener) {

        queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res) {

                        listener.onDataRetrieved(res);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("App", "onErrorResponse " + error.toString());

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

    public interface Listener {

        void onDataRetrieved(String result);
    }
}
