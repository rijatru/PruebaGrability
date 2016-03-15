package com.ricardotrujillo.prueba.workers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.ricardotrujillo.prueba.Constants;
import com.ricardotrujillo.prueba.db.DaoMaster;
import com.ricardotrujillo.prueba.db.DaoSession;
import com.ricardotrujillo.prueba.db.Store;
import com.ricardotrujillo.prueba.db.StoreDao;

import java.util.List;

import javax.inject.Inject;

public class DbWorker {

    @Inject
    public DbWorker() {

    }

    public void saveObject(Context context, Object object) {

        com.ricardotrujillo.prueba.db.Store daoStore = new com.ricardotrujillo.prueba.db.Store();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "store-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();

        Log.d("Test", "Object to save: " + new Gson().toJson(object));

        daoStore.setObject(new Gson().toJson(object));
        daoStore.setObjectId(Constants.STORE_ID);

        StoreDao storeDao = daoSession.getStoreDao();

        storeDao.insertOrReplace(daoStore);
    }

    public Object getObject(Context context) {

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "store-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();

        StoreDao storeDao = daoSession.getStoreDao();
        List<Store> storeList = storeDao.loadAll();

        Log.d("Test", "Got from db: " + storeList.size());

        if (storeList.size() > 0) {

            return new Gson().fromJson(storeList.get(0).getObject(), com.ricardotrujillo.prueba.model.Store.class);
        }

        return null;
    }
}
