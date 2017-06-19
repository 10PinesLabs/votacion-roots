package ar.com.kfgodel.temas.application;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.dependencies.impl.DependencyInjectorImpl;
import ar.com.kfgodel.orm.api.HibernateOrm;
import ar.com.kfgodel.orm.api.config.DbCoordinates;
import ar.com.kfgodel.orm.api.operations.basic.Save;
import ar.com.kfgodel.orm.impl.HibernateFacade;
import ar.com.kfgodel.temas.application.initializers.InicializadorDeDatos;
import ar.com.kfgodel.temas.config.TemasConfiguration;
import ar.com.kfgodel.transformbyconvention.api.TypeTransformer;
import ar.com.kfgodel.transformbyconvention.impl.B2BTransformer;
import ar.com.kfgodel.transformbyconvention.impl.config.TransformerConfigurationByConvention;
import ar.com.kfgodel.webbyconvention.api.WebServer;
import ar.com.kfgodel.webbyconvention.api.auth.WebCredential;
import ar.com.kfgodel.webbyconvention.api.config.WebServerConfiguration;
import ar.com.kfgodel.webbyconvention.impl.JettyWebServer;
import ar.com.kfgodel.webbyconvention.impl.config.ConfigurationByConvention;
import convention.persistent.Reunion;
import convention.persistent.Usuario;
import convention.rest.api.tos.BackofficeUserTo;
import convention.rest.api.tos.ReunionTo;
import convention.rest.api.tos.UserTo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Optional;
import java.util.function.Function;

/**
 * This type represents the whole application as a single object.<br>
 * From this instance you can access the application components
 * <p/>
 * Created by kfgodel on 22/03/15.
 */
public class TemasApplication implements Application {
  public static Logger LOG = LoggerFactory.getLogger(TemasApplication.class);

  protected TemasConfiguration config;
  protected DependencyInjector injector;

  @Override
  public WebServer getWebServerModule() {
    return this.injector.getImplementationFor(WebServer.class).get();
  }

  @Override
  public HibernateOrm getOrmModule() {
    return this.injector.getImplementationFor(HibernateOrm.class).get();
  }

  @Override
  public TypeTransformer getTransformerModule() {
    return this.injector.getImplementationFor(TypeTransformer.class).get();
  }

  @Override
  public TemasConfiguration getConfiguration() {
    return config;
  }

  @Override
  public DependencyInjector getInjector() {
    return injector;
  }

  public static Application create(TemasConfiguration config) {
    TemasApplication application = new TemasApplication();
    application.config = config;
    return application;
  }

  /**
   * Initializes application components and starts the server to listen for requests
   */
  @Override
  public void start() {
    LOG.info("Starting APP");
    this.initialize();
    this.getWebServerModule().startAndJoin();
  }

  @Override
  public void stop() {
    LOG.info("Stopping APP");
    this.getWebServerModule().stop();
    this.getOrmModule().close();
  }

  private void initialize() {
    this.injector = config.getInjector();
    this.injector.bindTo(Application.class, this);

    this.injector.bindTo(HibernateOrm.class, createPersistenceLayer());
    // Web server depends on hibernate, so it needs to be created after hibernate
    this.injector.bindTo(WebServer.class, createWebServer());
    this.injector.bindTo(TypeTransformer.class, createTransformer());

    registerCleanupHook();

    InicializadorDeDatos.create(this).inicializar();
  }

  protected TypeTransformer createTransformer() {
    TransformerConfigurationByConvention configuration = TransformerConfigurationByConvention.create(this.getInjector());
    return B2BTransformer.create(configuration);
  }

  protected void registerCleanupHook() {
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      LOG.info("Cleaning-up before shutdown");
      this.stop();
    }, "cleanup-thread"));
  }

  protected HibernateOrm createPersistenceLayer() {
    DbCoordinates dbCoordinates = config.getDatabaseCoordinates();
    HibernateOrm hibernateOrm = HibernateFacade.createWithConventionsFor(dbCoordinates);
    return hibernateOrm;
  }

  private WebServer createWebServer() {
    WebServerConfiguration serverConfig = ConfigurationByConvention.create()
      .authenticatingWith(config.autenticador())
      //.authenticatingWith(BackofficeCallbackAuthenticator.create(getInjector()))
      .listeningHttpOn(config.getHttpPort())
      .withInjections((binder) -> {
        //Make application the only jetty injectable dependency
        binder.bind(this).to(Application.class);
      })
      .withSecuredRootPaths("/api/v1")
      .redirectingAfterSuccessfulAuthenticationTo("/")
      .redirectingAfterFailedAuthenticationTo("/login?failed=true");
    return JettyWebServer.createFor(serverConfig);
  }


}
