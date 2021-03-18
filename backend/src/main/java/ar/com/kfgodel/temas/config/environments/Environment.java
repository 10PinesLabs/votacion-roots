package ar.com.kfgodel.temas.config.environments;

import ar.com.kfgodel.temas.config.TemasConfiguration;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class Environment {

    private static List<Environment> enviroments = Arrays.asList(new Development(), new Staging(), new Production());

    public static Environment toHandle(String enviroment) {
        return enviroments.stream().filter(env -> env.canHandle(enviroment))
                .findFirst().orElse(new Development());
    }

    public abstract TemasConfiguration getConfig(Logger log);

    protected abstract boolean canHandle(String enviroment);

    public abstract String getHostName();

    public abstract String apiKey();

    protected String getEnv(String key) {
        return Optional.ofNullable(System.getenv(key))
            .orElseThrow(() -> new RuntimeException("Undefined environment variable: " + key));
    }
}
