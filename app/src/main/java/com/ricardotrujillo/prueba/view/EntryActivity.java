package com.ricardotrujillo.prueba.view;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;

import com.ricardotrujillo.prueba.App;
import com.ricardotrujillo.prueba.Constants;
import com.ricardotrujillo.prueba.R;
import com.ricardotrujillo.prueba.Utils;
import com.ricardotrujillo.prueba.adapter.StoreRecyclerViewAdapter;
import com.ricardotrujillo.prueba.controller.LogWorker;
import com.ricardotrujillo.prueba.databinding.ActivityEntryBinding;
import com.ricardotrujillo.prueba.helper.EntryActivityHelper;
import com.ricardotrujillo.prueba.model.Store;
import com.ricardotrujillo.prueba.model.StoreManager;
import com.squareup.picasso.Callback;

import javax.inject.Inject;

public class EntryActivity extends AppCompatActivity
        implements AppBarLayout.OnOffsetChangedListener {

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;
    @Inject
    StoreManager storeManager;
    @Inject
    LogWorker logWorker;
    Store.Feed.Entry entry;
    ActivityEntryBinding binding;
    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;
    private boolean isAnimatingAvatar = false;

    public static void startAlphaAnimation(View v, long duration, int visibility) {

        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inject();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_entry);
        binding.appbar.addOnOffsetChangedListener(this);
        binding.toolbar.inflateMenu(R.menu.sample_actions);

        setSupportActionBar(binding.toolbar);

        if(getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(null);
        }

        startAlphaAnimation(binding.textviewTitle, 0, View.INVISIBLE);

        initTransition();

        if (savedInstanceState == null) {

            if (getIntent().getExtras() != null) {

                getEntry(getIntent().getExtras().getInt(Constants.POSITION));

                if (entry != null) setUpBarColor(entry.paletteColor);
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

        if (savedInstanceState != null) {

            getEntry(savedInstanceState.getInt(Constants.POSITION));

            if (entry != null) setUpBarColor(entry.paletteColor);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        if (menuItem.getItemId() == android.R.id.home) {

            supportFinishAfterTransition();
        }
        return super.onOptionsItemSelected(menuItem);
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

            if (!mIsTheTitleVisible) {

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

            if (mIsTheTitleContainerVisible) {

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

    void setUpBarColor(int color) {

        binding.toolbar.setBackgroundColor(Utils.alterColor(color, 0.9f));

        binding.framelayoutTitle.setBackgroundColor(Utils.alterColor(color, 0.9f));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Window window = getWindow();

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(Utils.alterColor(color, 0.7f));
        }
    }

    void getEntry(int position) {

        if (storeManager.getStore() != null) {

            entry = storeManager.getStore().feed.entry[position];

            binding.setEntry(entry);
            binding.setHandlers(this);

            EntryActivityHelper.loadEntry(this, binding, entry);

            binding.setClick(new StoreRecyclerViewAdapter.StoreClickHandler() {

                @Override
                public void onClick(View view) {

                    //supportFinishAfterTransition();
                }
            });

            binding.ivFeedCenterThumbContainer.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(final View v, MotionEvent event) {

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {

                        if (!isAnimatingAvatar) {

                            isAnimatingAvatar = true;

                            Utils.animateAvatar(v, new Callback() {
                                @Override
                                public void onSuccess() {

                                    isAnimatingAvatar = false;
                                }

                                @Override
                                public void onError() {

                                }
                            });

                            return true;

                        }
                    }

                    return false;
                }
            });
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