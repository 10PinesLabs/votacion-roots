package ar.com.kfgodel.temas.config.environments;

import ar.com.kfgodel.temas.config.TemasConfiguration;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;

public abstract class Environment {

    private static List<Environment> enviroments = Arrays.asList(new Development(), new Staging(), new Production());

    public static Environment toHandle(String enviroment) {
        return enviroments.stream().filter(env -> env.canHandle(enviroment))
                .findFirst().orElse(new Development());
    }

    public abstract TemasConfiguration getConfig(Logger log);

    protected abstract boolean canHandle(String enviroment);

    public abstract String getHostName();
}
