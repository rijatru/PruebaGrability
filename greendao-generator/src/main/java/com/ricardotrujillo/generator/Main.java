package com.ricardotrujillo.generator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class Main {

    public static void main(String[] args) throws Exception {

        Schema schema = new Schema(1, "com.ricardotrujillo.prueba.db");

        Entity daoStore = schema.addEntity("Store");

        daoStore.addIdProperty();
        daoStore.addStringProperty("objectId").unique();
        daoStore.addStringProperty("object");

        DaoGenerator dg = new DaoGenerator();

        dg.generateAll(schema, "./app/src/main/java");
    }
}
