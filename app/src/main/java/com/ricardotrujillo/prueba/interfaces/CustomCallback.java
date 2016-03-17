package com.ricardotrujillo.prueba.interfaces;

public interface CustomCallback {

    void onSuccess();

    void onError();

    public static class EmptyCallback implements CustomCallback {

        @Override public void onSuccess() {
        }

        @Override public void onError() {
        }
    }
}
