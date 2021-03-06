package com.ricardotrujillo.prueba.model;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import java.util.HashMap;

import javax.inject.Inject;

public class StoreManager {

    HashMap<String, ColorDrawable> colorDrawables = new HashMap<>();
    private Store store;
    //private ColorDrawable[] colorDrawables;
    private Drawable[] drawables;

    @Inject
    public StoreManager() {

    }

    public void addStore(Store store) {

        this.store = store;

        drawables = new Drawable[store.feed.entry.size()];

        for (int i = 0; i < store.feed.entry.size(); i++) {

            store.feed.entry.get(i).name.entryLabel = store.feed.entry.get(i).name.label;
            store.feed.entry.get(i).name.label = (i + 1) + ". " + store.feed.entry.get(i).name.label;

            store.feed.entry.get(i).summary.label = store.feed.entry.get(i).summary.label.length() < 400 ?
                    store.feed.entry.get(i).summary.label : store.feed.entry.get(i).summary.label.substring(0, 400) + "...";
        }
    }

    public Store getStore() {

        return store;
    }

    public void addDrawables(int position, ColorDrawable colorDrawable) {

        colorDrawables.put(store.feed.entry.get(position).name.label, colorDrawable);
    }

    //public Drawable getDrawable(int position) {

    //    return colorDrawables[position];
    //}

    public ColorDrawable getColorDrawable(String name) {

        return colorDrawables.get(name);
    }

    public int getDrawablesSize() {

        return drawables.length;
    }
}
