package com.ricardotrujillo.prueba.helper;

import android.animation.Animator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.ricardotrujillo.prueba.Constants;
import com.ricardotrujillo.prueba.R;
import com.ricardotrujillo.prueba.Utils;
import com.ricardotrujillo.prueba.databinding.ActivityEntryBinding;
import com.ricardotrujillo.prueba.model.Store;
import com.ricardotrujillo.prueba.workers.NetWorker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by Ricardo on 18/03/2016.
 */
public class EntryActivityHelper {

    public static void loadEntry(final Activity activity, final ActivityEntryBinding binding, Store.Feed.Entry entry) {

        Picasso.with(activity)
                .load(entry.image[2].label)
                .networkPolicy(
                        NetWorker.isConnected(activity) ?
                                NetworkPolicy.NO_CACHE : NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.img_feed_center_1)
                .into(binding.ivFeedCenterThumb, new Callback() {
                    @Override
                    public void onSuccess() {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                            final Bitmap bitmap = ((BitmapDrawable) binding.ivFeedCenterThumb.getDrawable()).getBitmap();

                            Bitmap newBitmap = bitmap.copy(bitmap.getConfig(), bitmap.isMutable() ? true : false);

                            binding.ivFeedCenter.setImageBitmap(Utils.blur(activity, newBitmap, 6f));

                            binding.ivFeedCenter.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {

                                @Override
                                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                                    v.removeOnLayoutChangeListener(this);

                                    binding.ivFeedCenter.setAlpha(1f);

                                    Utils.enterReveal(binding.ivFeedCenter);
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
    }

    public static void animateExit(final ActivityEntryBinding binding, final Callback callback) {

        binding.ivFeedCenter
                .animate()
                .setDuration(Constants.EXIT_VIEW_MAIN_PANEL)
                .y(binding.ivFeedCenter.getY() - binding.ivFeedCenter.getHeight())
                .setInterpolator(new AccelerateDecelerateInterpolator());
    }
}
