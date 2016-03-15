package com.ricardotrujillo.generator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class Main {

    public static void main(String[] args) throws Exception {

        Schema schema = new Schema(1, "com.ricardotrujillo.prueba.db");

        Entity program = schema.addEntity("Store");

        program.addIdProperty();
        program.addStringProperty("name");

        DaoGenerator dg = new DaoGenerator();

        dg.generateAll(schema, "./app/src/main/java");
    }
}
