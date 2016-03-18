package com.ricardotrujillo.prueba.activities;

import android.annotation.TargetApi;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionInflater;
import android.view.View;

import com.ricardotrujillo.prueba.App;
import com.ricardotrujillo.prueba.Constants;
import com.ricardotrujillo.prueba.R;
import com.ricardotrujillo.prueba.Utils;
import com.ricardotrujillo.prueba.adapter.StoreRecyclerViewAdapter;
import com.ricardotrujillo.prueba.databinding.ActivityEntryBinding;
import com.ricardotrujillo.prueba.model.Store;
import com.ricardotrujillo.prueba.model.StoreManager;
import com.ricardotrujillo.prueba.workers.LogWorker;
import com.ricardotrujillo.prueba.workers.NetWorker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

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

                entry = storeManager.getStore().feed.entry[getIntent().getExtras().getInt(Constants.POSITION)];

                binding.setEntry(entry);
                binding.setHandlers(this);

                loadEntry();

                binding.setClick(new StoreRecyclerViewAdapter.StoreClickHandler() {

                    @Override
                    public void onClick(View view) {

                        onClickButton(view);
                    }
                });
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt(Constants.POSITION, getIntent().getExtras().getInt(Constants.POSITION));
        savedInstanceState.putString("MyString", "Welcome back to Android");
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_entry);

        entry = storeManager.getStore().feed.entry[savedInstanceState.getInt(Constants.POSITION)];

        loadEntry();
    }

    @Override
    public void onBackPressed() {

        supportFinishAfterTransition();
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

    void loadEntry() {

        binding.ivFeedCenter.setAlpha(0f);

        Picasso.with(this)
                .load(entry.image[1].label)
                .networkPolicy(
                        NetWorker.isConnected(this) ?
                                NetworkPolicy.NO_CACHE : NetworkPolicy.OFFLINE)
                .into(binding.ivFeedCenter, new Callback() {
                    @Override
                    public void onSuccess() {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                            Bitmap bitmap = ((BitmapDrawable) binding.ivFeedCenter.getDrawable()).getBitmap();

                            binding.ivFeedCenter.setImageBitmap(Utils.blur(EntryActivity.this, bitmap, 6f));

                            binding.ivFeedCenter.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {

                                    @Override
                                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                                        v.removeOnLayoutChangeListener(this);

                                        binding.ivFeedCenter.setAlpha(1f);

                                        Utils.enterReveal(binding.ivFeedCenter);


                                        //binding.rlMainPane.setY(binding.rlMainPane.getY() + binding.rlMainPane.getHeight() / 2);

                                        //binding.rlMainPane.animate().setDuration(1000).y(binding.getRoot().getHeight() - binding.rlMainPane.getHeight());
                                    }
                                });

                        } else {

                            binding.ivFeedCenter.setVisibility(View.VISIBLE);

                            binding.ivFeedCenter.animate().alpha(1f);
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });

        Picasso.with(this)
                .load(entry.image[2].label)
                .networkPolicy(
                        NetWorker.isConnected(this) ?
                                NetworkPolicy.NO_CACHE : NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.img_feed_center_1)
                .into(binding.ivFeedCenterThumb);
    }

    void inject() {

        ((App) getApplication()).getAppComponent().inject(this);
    }
}
