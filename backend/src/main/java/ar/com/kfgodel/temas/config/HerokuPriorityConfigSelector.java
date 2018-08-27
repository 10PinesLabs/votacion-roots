package ar.com.kfgodel.temas.config;

import ar.com.kfgodel.temas.config.environments.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This type represents the configuration selector that picks a configuration based on the presence of heroku environment variables
 * to determine the best configuration
 * Created by kfgodel on 13/03/16.
 */
public class HerokuPriorityConfigSelector implements ConfigurationSelector {
  public static Logger LOG = LoggerFactory.getLogger(HerokuPriorityConfigSelector.class);

  public static HerokuPriorityConfigSelector create() {
    HerokuPriorityConfigSelector selector = new HerokuPriorityConfigSelector();
    return selector;
  }

  @Override
  public TemasConfiguration selectConfig() {
    return Environment.toHandle(System.getenv("ENVIROMENT")).getConfig(LOG);
  }
}