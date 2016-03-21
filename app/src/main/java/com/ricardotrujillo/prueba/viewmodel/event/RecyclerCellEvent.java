package com.ricardotrujillo.prueba.viewmodel.event;

import android.view.View;

public class RecyclerCellEvent {

    String string;
    View view;
    int position;

    public RecyclerCellEvent(String string) {

        this.string = string;
    }

    public RecyclerCellEvent(String string, View view, int position) {

        this.string = string;
        this.view = view;
        this.position = position;
    }

    public String getString() {

        return string;
    }

    public View getView() {

        return view;
    }

    public int getPosition() {

        return position;
    }
}
