package ar.com.kfgodel.temas.helpers;

import ar.com.kfgodel.temas.application.TemasApplication;
import ar.com.kfgodel.temas.config.TemasConfiguration;

public class ResourceTestApplication extends TemasApplication {

    public ResourceTestApplication(TemasConfiguration configuration) {
        setConfiguration(configuration);
    }

    @Override
    public void start() {
        this.initialize();
        this.getWebServerModule().startAndJoin();
    }
}
