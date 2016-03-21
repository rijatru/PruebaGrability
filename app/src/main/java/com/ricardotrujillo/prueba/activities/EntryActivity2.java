package com.ricardotrujillo.prueba.activities;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.Menu;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ricardotrujillo.prueba.App;
import com.ricardotrujillo.prueba.Constants;
import com.ricardotrujillo.prueba.R;
import com.ricardotrujillo.prueba.adapter.StoreRecyclerViewAdapter;
import com.ricardotrujillo.prueba.databinding.ActivityEntry2Binding;
import com.ricardotrujillo.prueba.databinding.ActivityEntryBinding;
import com.ricardotrujillo.prueba.helper.EntryActivityHelper;
import com.ricardotrujillo.prueba.model.Store;
import com.ricardotrujillo.prueba.model.StoreManager;
import com.ricardotrujillo.prueba.workers.LogWorker;

import javax.inject.Inject;

public class EntryActivity2 extends AppCompatActivity
        implements AppBarLayout.OnOffsetChangedListener {

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;

    private boolean mIsTheTitleVisible          = false;
    private boolean mIsTheTitleContainerVisible = true;

    @Inject
    StoreManager storeManager;
    @Inject
    LogWorker logWorker;

    Store.Feed.Entry entry;

    ActivityEntry2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_entry2);

        binding.appbar.addOnOffsetChangedListener(this);

        binding.toolbar.inflateMenu(R.menu.sample_actions);

        startAlphaAnimation(binding.textviewTitle, 0, View.INVISIBLE);

        inject();

        initTransition();

        if (savedInstanceState == null) {

            if (getIntent().getExtras() != null) {

                getEntry();

                setUpBarColor(entry.paletteColor);
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

        binding = DataBindingUtil.setContentView(this, R.layout.activity_entry2);

        entry = storeManager.getStore().feed.entry[savedInstanceState.getInt(Constants.POSITION)];

        EntryActivityHelper.loadEntry(this, binding, entry);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if(!mIsTheTitleVisible) {
                startAlphaAnimation(binding.textviewTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(binding.textviewTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if(mIsTheTitleContainerVisible) {
                startAlphaAnimation(binding.linearlayoutTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(binding.linearlayoutTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation (View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    void setUpBarColor(int color) {

        binding.toolbar.setBackgroundColor(color);

        binding.framelayoutTitle.setBackgroundColor(color);
    }

    void getEntry() {

        entry = storeManager.getStore().feed.entry[getIntent().getExtras().getInt(Constants.POSITION)];

        binding.setEntry(entry);
        binding.setHandlers(this);

        EntryActivityHelper.loadEntry(this, binding, entry);

        binding.setClick(new StoreRecyclerViewAdapter.StoreClickHandler() {

            @Override
            public void onClick(View view) {

                supportFinishAfterTransition();
            }
        });
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