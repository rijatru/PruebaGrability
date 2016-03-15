package com.ricardotrujillo.prueba.workers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ricardotrujillo.prueba.db.DaoMaster;
import com.ricardotrujillo.prueba.db.DaoSession;
import com.ricardotrujillo.prueba.db.StoreDao;

import java.util.List;

import javax.inject.Inject;

public class DbWorker {

    @Inject
    public DbWorker() {

    }

    public void saveObject(Context context, Object object) {

        com.ricardotrujillo.prueba.db.Store daoStore = new com.ricardotrujillo.prueba.db.Store();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "lease-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();

        //daoStore.setObject();

        StoreDao personDao = daoSession.getStoreDao();
        personDao.insertOrReplace(daoStore);
    }

    Object getObject(Context context) {

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "lease-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();

        StoreDao storeDao = daoSession.getStoreDao();
        List storeList = storeDao.loadAll();

        return storeList.get(0);
    }
}
