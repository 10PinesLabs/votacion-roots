package ar.com.kfgodel.temas.persistence;

import ar.com.kfgodel.temas.application.Application;
import ar.com.kfgodel.temas.application.TemasApplication;
import ar.com.kfgodel.temas.config.TemasConfiguration;
/**
 * Created by sandro on 19/06/17.
 */
public class TestApplication extends TemasApplication{


    @Override
    public void start() {
        this.initialize();
    }

    public static TestApplication create(TemasConfiguration config) {
        TestApplication application = new TestApplication();
        application.setConfiguration(config);
        return application;
    }


}
