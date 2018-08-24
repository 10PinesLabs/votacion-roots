package ar.com.kfgodel.temas.config;

import ar.com.kfgodel.temas.application.auth.BackofficeCallbackAuthenticatorForRootsOnly;
import ar.com.kfgodel.webbyconvention.api.auth.WebCredential;

import java.util.Optional;
import java.util.function.Function;

public class HerokuProductionConfig extends HerokuConfig{
    public static HerokuConfig create() {
        HerokuConfig config = new HerokuProductionConfig();
        return config;
    }

    public Function<WebCredential, Optional<Object>> autenticador() {
        return BackofficeCallbackAuthenticatorForRootsOnly.create(getInjector());
    }
}
