package ar.com.kfgodel.temas.config;

import ar.com.kfgodel.temas.helpers.TestConfig;
import ar.com.kfgodel.webbyconvention.api.auth.WebCredential;
import convention.rest.api.tos.UserTo;

import java.util.Optional;
import java.util.function.Function;

public class AuthenticatedTestConfig extends TestConfig {

    @Override
    public Function<WebCredential, Optional<Object>> autenticador() {
        return webCredential -> Optional.of(getAuthenticatedUserId());
    }

    public Long getAuthenticatedUserId() {
        return getUsers().stream().map(UserTo::getId).min(Long::compareTo).get();
    }
}
