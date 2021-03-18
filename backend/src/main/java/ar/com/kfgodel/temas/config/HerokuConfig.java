package ar.com.kfgodel.temas.config;

import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.dependencies.impl.DependencyInjectorImpl;
import ar.com.kfgodel.orm.api.config.DbCoordinates;
import ar.com.kfgodel.orm.impl.config.ImmutableDbCoordinates;
import ar.com.kfgodel.temas.application.auth.BackofficeCallbackAuthenticatorForRootsOnly;
import ar.com.kfgodel.webbyconvention.api.auth.WebCredential;
import com.heroku.sdk.jdbc.DatabaseUrl;

import java.net.URISyntaxException;
import java.util.Optional;
import java.util.function.Function;

/**
 * This type represents the configuration used to run in heroku with values taken from the OS environment
 * Created by kfgodel on 13/03/16.
 */
public abstract class HerokuConfig implements TemasConfiguration {
  private DependencyInjector injector= DependencyInjectorImpl.create();

  @Override
  public DbCoordinates getDatabaseCoordinates() {
    DatabaseUrl herokuCoordinates = null;
    try {
      herokuCoordinates = DatabaseUrl.extract();
    } catch (URISyntaxException e) {
      throw new RuntimeException("Error accessing heroku jdbc url", e);
    }
    String url = herokuCoordinates.jdbcUrl().replace("ssl=true", "sslmode=require");
    String userName = herokuCoordinates.username();
    String password = herokuCoordinates.password();
    return ImmutableDbCoordinates.createDeductingDialect(url, userName, password);
  }

  @Override
  public int getHttpPort() {
    return Integer.valueOf(System.getenv("PORT"));
  }

  @Override
  public abstract Function<WebCredential, Optional<Object>> autenticador();

  @Override
  public DependencyInjector getInjector() {
    return injector;
  }
}
