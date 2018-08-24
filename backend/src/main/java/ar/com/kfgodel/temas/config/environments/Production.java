package ar.com.kfgodel.temas.config.environments;

import ar.com.kfgodel.temas.config.HerokuProductionConfig;
import ar.com.kfgodel.temas.config.TemasConfiguration;
import org.slf4j.Logger;

public class Production extends Environment {
    @Override
    public TemasConfiguration getConfig(Logger log) {
        log.info("Using Heroku-Production configuration");
        return HerokuProductionConfig.create();
    }

    @Override
    protected boolean canHandle(String environment) {
        return "PROD".equals(environment);
    }

}
