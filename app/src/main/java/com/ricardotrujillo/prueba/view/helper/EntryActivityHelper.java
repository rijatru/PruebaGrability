package com.ricardotrujillo.prueba.view.helper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.View;

import com.ricardotrujillo.prueba.R;
import com.ricardotrujillo.prueba.databinding.ActivityEntryBinding;
import com.ricardotrujillo.prueba.model.Store;
import com.ricardotrujillo.prueba.viewmodel.worker.NetWorker;
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

                        final Bitmap bitmap = ((BitmapDrawable) binding.ivFeedCenterThumb.getDrawable()).getBitmap();

                        Bitmap newBitmap = bitmap.copy(bitmap.getConfig(), bitmap.isMutable());

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

                            binding.ivFeedCenter.setImageBitmap(ViewUtils.blur(activity, newBitmap, 7f));

                            binding.ivFeedCenter.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {

                                @Override
                                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                                    v.removeOnLayoutChangeListener(this);

                                    ViewUtils.enterReveal(binding.ivFeedCenter);
                                }
                            });

                        } else {

                            binding.ivFeedCenter.setImageBitmap(newBitmap);
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

}
