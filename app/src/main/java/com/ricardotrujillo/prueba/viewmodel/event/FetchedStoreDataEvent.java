package com.ricardotrujillo.prueba.viewmodel.event;

public class FetchedStoreDataEvent {

    int position;

    public FetchedStoreDataEvent() {

    }

    public FetchedStoreDataEvent(int position) {

        this.position = position;
    }

    public int getPosition() {

        return this.position;
    }
}
