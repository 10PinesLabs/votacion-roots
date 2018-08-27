package ar.com.kfgodel.temas.config.environments;

import ar.com.kfgodel.temas.config.HerokuStagingConfig;
import ar.com.kfgodel.temas.config.TemasConfiguration;
import org.slf4j.Logger;

public class Staging extends Environment {

    private String environmentVariable = "STG";

    @Override
    public TemasConfiguration getConfig(Logger log) {
        log.info("Using Heroku-Staging configuration");
        return HerokuStagingConfig.create();
    }

    @Override
    protected boolean canHandle(String enviroment) {
        return environmentVariable.equals(enviroment);
    }
}
