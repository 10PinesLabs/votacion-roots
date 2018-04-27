package ar.com.kfgodel.temas.application.migrations;

import org.flywaydb.core.Flyway;

public class Migrator {
    private Flyway flyway;
    public Migrator(String url,String username,String pass){
        flyway = new Flyway();
        flyway.setDataSource(url,username,pass);
    }

    public void init(){
        flyway.setBaselineOnMigrate(true);
        flyway.repair();
        flyway.migrate();
    }

}