package com.ricardotrujillo.prueba.model;

import android.databinding.BindingAdapter;
import android.util.Log;
import android.widget.ImageView;

import com.ricardotrujillo.prueba.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Ricardo on 16/03/2016.
 */
public class EntryViewModel {

    public String getImageUrl() {
        // The URL will usually come from a model (i.e Profile)
        return "http://cdn.meme.am/instances/60677654.jpg";
    }

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {

        Log.d("Test", "loadImage: " + imageUrl);

        Picasso.with(view.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.img_feed_center_1)
                .into(view);
    }
}