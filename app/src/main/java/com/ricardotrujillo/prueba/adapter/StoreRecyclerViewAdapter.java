/*
* Copyright (C) 2014 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.ricardotrujillo.prueba.adapter;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ricardotrujillo.prueba.App;
import com.ricardotrujillo.prueba.Constants;
import com.ricardotrujillo.prueba.R;
import com.ricardotrujillo.prueba.activities.EntryActivity;
import com.ricardotrujillo.prueba.databinding.StoreRowBinding;
import com.ricardotrujillo.prueba.model.EntryViewModel;
import com.ricardotrujillo.prueba.model.RecyclerCellEvent;
import com.ricardotrujillo.prueba.model.Store;
import com.ricardotrujillo.prueba.model.StoreManager;
import com.ricardotrujillo.prueba.view.LoadingFeedItemView;
import com.ricardotrujillo.prueba.workers.BusWorker;
import com.ricardotrujillo.prueba.workers.LogWorker;

import javax.inject.Inject;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class StoreRecyclerViewAdapter extends RecyclerView.Adapter<StoreRecyclerViewAdapter.BindingHolder> {

    public static final String ACTION_LIKE_BUTTON_CLICKED = "action_like_button_button";
    public static final String ACTION_LIKE_IMAGE_CLICKED = "action_like_image_button";
    public static final int VIEW_TYPE_DEFAULT = 1;
    public static final int VIEW_TYPE_LOADER = 2;

    @Inject
    LogWorker logWorker;
    @Inject
    BusWorker busWorker;
    @Inject
    StoreManager storeManager;

    static Activity activity;
    private int itemsCount = 0;
    private int lastAnimatedPosition = -1;
    private int avatarSize;
    private boolean animationsLocked = false;
    private boolean delayEnterAnimation = true;

    private boolean showLoadingView = false;

    public StoreRecyclerViewAdapter(Activity act) {

        activity = act;

        inject();
    }

    void inject() {

        ((App) activity.getApplication()).getAppComponent().inject(this);
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int type) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        StoreRowBinding binding = StoreRowBinding.inflate(inflater, parent, false);

        return new BindingHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(final BindingHolder holder, int position) {

        final Store.Feed.Entry entry = storeManager.getStore().feed.entry[position];

        holder.binding.setEntry(entry);

        holder.binding.setClick(new StoreClickHandler() {

            @Override
            public void onClick(View view) {

                onClickButton(view, holder);
            }
        });

        holder.getBinding().executePendingBindings();

        if (getItemViewType(position) == VIEW_TYPE_LOADER) {

            bindLoadingFeedItem((LoadingCellFeedViewHolder) holder);
        }
    }

    private void bindLoadingFeedItem(final LoadingCellFeedViewHolder holder) {
        holder.loadingFeedItemView.setOnLoadingFinishedListener(new LoadingFeedItemView.OnLoadingFinishedListener() {
            @Override
            public void onLoadingFinished() {
                showLoadingView = false;
                notifyItemChanged(0);
            }
        });
        holder.loadingFeedItemView.startLoading();
    }

    @Override
    public int getItemViewType(int position) {
        if (showLoadingView && position == 0) {
            return VIEW_TYPE_LOADER;
        } else {
            return VIEW_TYPE_DEFAULT;
        }
    }

    public void onClickButton(View view, BindingHolder holder) {

        switch (view.getId()) {

            case R.id.btnLike:

                int adapterPosition = holder.getAdapterPosition();
                holder.binding.getEntry().likes = holder.binding.getEntry().likes + 1;
                holder.binding.getEntry().isLiked = true;
                notifyItemChanged(adapterPosition, ACTION_LIKE_BUTTON_CLICKED);

                busWorker.getBus().post(new RecyclerCellEvent(Constants.LIKE));

                logWorker.log("Click on btnLike " + holder.binding.getEntry().name.label);

                break;

            case R.id.btnComments:

                logWorker.log("Click on btnComments " + holder.binding.getEntry().name.label);

                break;

            case R.id.btnMore:

                busWorker.getBus().post(new RecyclerCellEvent(Constants.MORE, holder.getBinding().btnMore, holder.getAdapterPosition()));

                logWorker.log("Click on btnMore " + holder.binding.getEntry().name.label);

                break;

            case R.id.ivFeedCenter:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    holder.binding.ivFeedCenter.setTransitionName(activity.getString(R.string.entry_transition_name));

                    Intent intent = new Intent(activity, EntryActivity.class);
                    intent.putExtra(Constants.POSITION, holder.getLayoutPosition());

                    ActivityOptionsCompat options = ActivityOptionsCompat
                            .makeSceneTransitionAnimation(
                                    activity,
                                    holder.binding.ivFeedCenter,
                                    holder.binding.ivFeedCenter.getTransitionName());

                    activity.startActivity(intent, options.toBundle());

                } else {

                    Intent intent = new Intent(activity, EntryActivity.class);

                    ActivityOptionsCompat options = ActivityOptionsCompat
                            .makeSceneTransitionAnimation(
                                    activity,
                                    holder.binding.ivFeedCenter,
                                    holder.binding.ivFeedCenter.getTransitionName());

                    activity.startActivity(intent, options.toBundle());
                }



                int adapterPos = holder.getAdapterPosition();
                holder.binding.getEntry().likes = holder.binding.getEntry().likes + 1;
                holder.binding.getEntry().isLiked = true;
                notifyItemChanged(adapterPos, ACTION_LIKE_IMAGE_CLICKED);

                busWorker.getBus().post(new RecyclerCellEvent(Constants.LIKE));

                break;

            default:

                break;
        }
    }

    @Override
    public void onViewDetachedFromWindow(BindingHolder holder) {

        holder.clearAnimation();
    }

    @Override
    public int getItemCount() {

        if (storeManager.getStore() != null) {

            return storeManager.getStore().feed.entry.length;

        } else {

            return 0;
        }
    }

    public void setAnimationsLocked(boolean animationsLocked) {
        this.animationsLocked = animationsLocked;
    }

    public interface StoreClickHandler {

        void onClick(View view);
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {

        StoreRowBinding binding;

        public BindingHolder(View v) {

            super(v);

            binding = DataBindingUtil.bind(v);

            binding.setViewModel(new EntryViewModel(activity));
        }

        public StoreRowBinding getBinding() {

            return binding;
        }

        public void clearAnimation() {

            binding.getRoot().clearAnimation();
        }
    }

    public static class LoadingCellFeedViewHolder extends BindingHolder {

        LoadingFeedItemView loadingFeedItemView;

        public LoadingCellFeedViewHolder(LoadingFeedItemView view) {
            super(view);
            this.loadingFeedItemView = view;
        }
    }
}
