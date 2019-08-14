package ar.com.kfgodel.temas.services;

import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.temas.apiRest.JettyIdentityAdapterTest;
import ar.com.kfgodel.temas.apiRest.SecurityContextTest;
import ar.com.kfgodel.temas.helpers.TestConfig;
import ar.com.kfgodel.temas.helpers.TestHelper;
import ar.com.kfgodel.temas.persistence.TestApplication;
import convention.persistent.Usuario;
import convention.rest.api.DuracionesResource;
import convention.rest.api.ReunionResource;
import convention.rest.api.TemaDeReunionResource;
import convention.services.MinutaService;
import convention.services.ReunionService;
import convention.services.TemaService;
import convention.services.UsuarioService;
import org.junit.After;
import org.junit.Before;

import javax.ws.rs.core.SecurityContext;

public abstract class ServiceTest {
    private TestHelper helper = new TestHelper();

    TestApplication app;

    ReunionService reunionService;
    MinutaService minutaService;
    UsuarioService usuarioService;
    TemaService temaService;

    ReunionResource reunionResource;
    DuracionesResource duracionResource;

    SecurityContext testContextUserFeche;
    TemaDeReunionResource temaDeReunionResource;
    Long userId;
    Long otherUserId;
    Usuario otherUser;
    Usuario user;

    @Before
    public void setUp() {
        app = TestApplication.create(TestConfig.create());
        app.start();
        DependencyInjector injector = app.injector();
        temaDeReunionResource = TemaDeReunionResource.create(injector);
        duracionResource = DuracionesResource.create(injector);

        reunionResource = ReunionResource.create(injector);
        temaService = injector.getImplementationFor(TemaService.class).get();
        usuarioService = injector.createInjected(UsuarioService.class);
        minutaService = injector.getImplementationFor(MinutaService.class).get();
        reunionService = injector.getImplementationFor(ReunionService.class).get();

        user = usuarioService.save(helper.unFeche());
        otherUser = usuarioService.save(helper.unSandro());

        testContextUserFeche = new SecurityContextTest(usuarioService.getAll().get(0).getId());
        userId = ((JettyIdentityAdapterTest) testContextUserFeche.getUserPrincipal()).getApplicationIdentification();
        otherUserId = otherUser.getId();
    }

    @After
    public void drop() {
        app.clearServices();
    }
}
