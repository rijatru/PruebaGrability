package com.ricardotrujillo.prueba.model;

import javax.inject.Inject;

public class StoreManager {

    private Store store;

    @Inject
    public StoreManager() {

    }

    public void addStore(Store store) {

        this.store = store;
    }

    public Store getStore() {

        return store;
    }
}
