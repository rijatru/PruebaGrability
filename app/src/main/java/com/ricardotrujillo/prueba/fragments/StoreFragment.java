    package com.ricardotrujillo.prueba.fragments;

    import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
    import android.content.res.Configuration;
    import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ricardotrujillo.prueba.App;
import com.ricardotrujillo.prueba.Constants;
import com.ricardotrujillo.prueba.R;
import com.ricardotrujillo.prueba.adapter.FeedItemAnimator;
import com.ricardotrujillo.prueba.adapter.StoreRecyclerViewAdapter;
import com.ricardotrujillo.prueba.databinding.StoreFragmentBinding;
import com.ricardotrujillo.prueba.model.FetchedStoreDataEvent;
import com.ricardotrujillo.prueba.model.StoreManager;
import com.ricardotrujillo.prueba.view.FeedContextMenuManager;
import com.ricardotrujillo.prueba.workers.BusWorker;
import com.ricardotrujillo.prueba.workers.LogWorker;
import com.ricardotrujillo.prueba.workers.NetWorker;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

public class StoreFragment extends Fragment {

    public static StoreRecyclerViewAdapter adapter;
    private final int SPAN_COUNT = 3;
    private final int DATASET_COUNT = 15;
    private final String KEY_LAYOUT_MANAGER = "layoutManager";
    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView.LayoutManager mLayoutManager;

    @Inject
    BusWorker busWorker;
    @Inject
    LogWorker logWorker;
    @Inject
    NetWorker netWorker;
    @Inject
    StoreManager storeManager;

    StoreFragmentBinding binding;

    private boolean pendingIntroAnimation = true;

    public StoreFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inject();
    }

    @Override
    public void onPause() {
        super.onPause();

        busWorker.unRegister(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        busWorker.register(this);
    }

    void inject() {

        ((App) getActivity().getApplication()).getAppComponent().inject(this);
    }

    @Subscribe
    public void recievedMessage(FetchedStoreDataEvent event) {

        logWorker.log("recievedMessage Fragment " + (binding.storeRecyclerView.getAdapter() == null));

        adapter.notifyDataSetChanged();

        if (pendingIntroAnimation) {

            pendingIntroAnimation = false;

            updateItems(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.store_fragment, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        initRecyclerView(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType); // Save currently selected layout manager.

        super.onSaveInstanceState(savedInstanceState);
    }

    void initRecyclerView(Bundle savedInstanceState) {

        mLayoutManager = new LinearLayoutManager(getActivity());

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        } else {

            mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
        }

        if (savedInstanceState != null) {

           // mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    //.getSerializable(KEY_LAYOUT_MANAGER);
        }

        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        adapter = new StoreRecyclerViewAdapter(getActivity());
        binding.storeRecyclerView.setAdapter(adapter);
        binding.storeRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                FeedContextMenuManager.getInstance().onScrolled(recyclerView, dx, dy);
            }
        });

        binding.storeRecyclerView.setItemAnimator(new FeedItemAnimator());
    }

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {

        int scrollPosition = 0;

        if (binding.storeRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) binding.storeRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        binding.storeRecyclerView.setLayoutManager(mLayoutManager);
        binding.storeRecyclerView.scrollToPosition(scrollPosition);
    }

    public void updateItems(boolean animated) {

        if (animated) {

            logWorker.log("updateItems netWorker: " + netWorker.getScreenHeight());

            binding.storeRecyclerView.setY(netWorker.getScreenHeight());
            binding.storeRecyclerView
                    .animate()
                    .y(0f)
                    .setDuration(Constants.RECYCLER_INTRO_ANIM_DURATION)
                    .setInterpolator(new FastOutSlowInInterpolator())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);

                            busWorker.getBus().post(new FetchedStoreDataEvent());
                        }
                    });
        }
    }

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }
}
