package ar.com.kfgodel.temas.config;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.dependencies.impl.DependencyInjectorImpl;
import ar.com.kfgodel.diamond.api.types.reference.ReferenceOf;
import ar.com.kfgodel.orm.api.config.DbCoordinates;
import ar.com.kfgodel.orm.impl.config.ImmutableDbCoordinates;
import ar.com.kfgodel.temas.application.auth.BackofficeCallbackAuthenticatorForAll;
import ar.com.kfgodel.temas.filters.users.FindAllUsersOrderedByName;
import ar.com.kfgodel.webbyconvention.api.auth.WebCredential;
import convention.rest.api.tos.UserTo;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * This type represents the configuration for a development environment
 * Created by kfgodel on 12/03/16.
 */
public class DevelopmentConfig implements TemasConfiguration {

  private DependencyInjector injector=DependencyInjectorImpl.create();

  public static DevelopmentConfig create() {
    DevelopmentConfig config = new DevelopmentConfig();
    return config;
  }

  @Override
  public DbCoordinates getDatabaseCoordinates() {
    return ImmutableDbCoordinates.createDeductingDialect("jdbc:postgresql://localhost:5433/votacion","user","password");
  }

  @Override
  public int getHttpPort() {
    return 9090;
  }

  @Override
  public Function<WebCredential, Optional<Object>> autenticador() {
    return BackofficeCallbackAuthenticatorForAll.create(getInjector());
  }

  @Override
  public DependencyInjector getInjector() {
    return injector;
  }

  protected List<UserTo> getUsers() {
    return ApplicationOperation.createFor(getInjector()).
            insideASession().
            applying(FindAllUsersOrderedByName.create())
            .convertTo(new ReferenceOf<List<UserTo>>() {
            }.getReferencedType());
  }
}

