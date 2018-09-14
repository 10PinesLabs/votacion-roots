package ar.com.kfgodel.temas.config.environments;

import ar.com.kfgodel.temas.config.DevelopmentConfig;
import ar.com.kfgodel.temas.config.TemasConfiguration;
import org.slf4j.Logger;

public class Development extends Environment {

    @Override
    public TemasConfiguration getConfig(Logger log) {
        log.info("Using development configuration");
        return DevelopmentConfig.create();
    }

    @Override
    protected boolean canHandle(String enviroment) {
        return false;
    }

    @Override
    public String getHostName() {
        return "localhost:4200";
    }
}
