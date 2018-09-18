package ar.com.kfgodel.temas.config.environments;

import ar.com.kfgodel.temas.config.HerokuProductionConfig;
import ar.com.kfgodel.temas.config.TemasConfiguration;
import org.slf4j.Logger;

public class Production extends Environment {
    private String environmentVariable = "PROD";

    @Override
    public TemasConfiguration getConfig(Logger log) {
        log.info("Using Heroku-Production configuration");
        return HerokuProductionConfig.create();
    }

    @Override
    protected boolean canHandle(String environment) {
        return environmentVariable.equals(environment);
    }

    @Override
    public String getHostName() {
        return System.getenv("TEMAS_ROOTS_HOST");
    }

}
