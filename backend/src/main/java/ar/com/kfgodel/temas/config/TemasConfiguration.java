package ar.com.kfgodel.temas.config;

import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.orm.api.config.DbCoordinates;
import ar.com.kfgodel.temas.application.migrations.Migrator;
import ar.com.kfgodel.webbyconvention.api.auth.WebCredential;

import java.util.Optional;
import java.util.function.Function;

/**
 * This type represents the configuration data for the application
 * Created by kfgodel on 12/03/16.
 */
public interface TemasConfiguration {

  /**
   * @return The database information to connect to it
   */
  DbCoordinates getDatabaseCoordinates();

  /**
   * @return The port number to use for the web server
   */
  int getHttpPort();

  Function<WebCredential,Optional<Object>> autenticador();

   DependencyInjector getInjector();
//esto es por el tipo que espera el apply (esta funcion se pasa como parametro del apply en el inicializador de datos)
  default Void inicializarDatos(){
    Migrator migrator = new Migrator(getDatabaseCoordinates().getDbUrl(),getDatabaseCoordinates().getDbUsername(),getDatabaseCoordinates().getDbPassword());
      migrator.init();
      return null;
  }
}
