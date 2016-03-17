package com.ricardotrujillo.prueba.model;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

import javax.inject.Inject;

public class StoreManager {

    private Store store;
    private Drawable[] drawables;
    private ColorDrawable[] colorDrawables;

    @Inject
    public StoreManager() {

    }

    public void addStore(Store store) {

        this.store = store;

        drawables = new Drawable[store.feed.entry.length];
        colorDrawables = new ColorDrawable[store.feed.entry.length];
    }

    public Store getStore() {

        return store;
    }

    public void addDrawables(int position, Drawable drawable, ColorDrawable colorDrawable) {

        drawables[position] = drawable;
        colorDrawables[position] = colorDrawable;
    }

    public Drawable getDrawable(int position) {

        return colorDrawables[position];
    }

    public ColorDrawable getColorDrawable(int position) {

        return colorDrawables[position];
    }

    public int getDrawablesSize() {

        return drawables.length;
    }
}
