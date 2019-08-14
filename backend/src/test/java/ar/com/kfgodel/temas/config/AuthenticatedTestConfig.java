package ar.com.kfgodel.temas.config;

import ar.com.kfgodel.temas.helpers.TestConfig;
import ar.com.kfgodel.webbyconvention.api.auth.WebCredential;

import java.util.Optional;
import java.util.function.Function;

public class AuthenticatedTestConfig extends TestConfig {

    @Override
    public Function<WebCredential, Optional<Object>> autenticador() {
        return webCredential -> Optional.of(getUsers().get(0).getId());
    }
}
