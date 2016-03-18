package com.ricardotrujillo.prueba.activities;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionInflater;
import android.view.View;

import com.ricardotrujillo.prueba.App;
import com.ricardotrujillo.prueba.Constants;
import com.ricardotrujillo.prueba.R;
import com.ricardotrujillo.prueba.adapter.StoreRecyclerViewAdapter;
import com.ricardotrujillo.prueba.databinding.ActivityEntryBinding;
import com.ricardotrujillo.prueba.helper.EntryActivityHelper;
import com.ricardotrujillo.prueba.model.Store;
import com.ricardotrujillo.prueba.model.StoreManager;
import com.ricardotrujillo.prueba.workers.LogWorker;
import com.squareup.picasso.Callback;

import javax.inject.Inject;

public class EntryActivity extends AppCompatActivity {

    @Inject
    StoreManager storeManager;
    @Inject
    LogWorker logWorker;

    ActivityEntryBinding binding;

    Store.Feed.Entry entry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_entry);

        inject();

        initTransition();

        if (savedInstanceState == null) {

            if (getIntent().getExtras() != null) {

                getEntry();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt(Constants.POSITION, getIntent().getExtras().getInt(Constants.POSITION));
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_entry);

        entry = storeManager.getStore().feed.entry[savedInstanceState.getInt(Constants.POSITION)];

        EntryActivityHelper.loadEntry(this, binding, entry);
    }

    @Override
    public void onBackPressed() {

        EntryActivityHelper.animateExit(binding, new Callback() {

            @Override
            public void onSuccess() {

                supportFinishAfterTransition();
            }

            @Override
            public void onError() {

            }
        });
    }

    void getEntry() {

        entry = storeManager.getStore().feed.entry[getIntent().getExtras().getInt(Constants.POSITION)];

        binding.setEntry(entry);
        binding.setHandlers(this);

        EntryActivityHelper.loadEntry(this, binding, entry);

        binding.setClick(new StoreRecyclerViewAdapter.StoreClickHandler() {

            @Override
            public void onClick(View view) {

                onClickButton(view);
            }
        });
    }

    public void onClickButton(View view) {

        switch (view.getId()) {

            case R.id.ivFeedCenter:

                logWorker.log("Clicked on ivFeedCenter");

                break;

            default:

                break;
        }
    }

    void initTransition() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.entry_transition));
        }
    }

    void inject() {

        ((App) getApplication()).getAppComponent().inject(this);
    }
}
