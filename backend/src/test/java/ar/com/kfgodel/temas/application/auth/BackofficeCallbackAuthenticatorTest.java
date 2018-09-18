package ar.com.kfgodel.temas.application.auth;

import ar.com.kfgodel.temas.application.Application;
import ar.com.kfgodel.temas.helpers.TestConfig;
import ar.com.kfgodel.temas.persistence.TestApplication;
import convention.persistent.Usuario;
import convention.rest.api.tos.BackofficeUserTo;
import convention.services.UsuarioService;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BackofficeCallbackAuthenticatorTest {

    private UsuarioService usuarioService;
    private Application application;

    @Before
    public void setUp() {
        startApplication();
        usuarioService = application.injector().createInjected(UsuarioService.class);
    }

    @Test
    public void siElUsuarioNoTieneMailEnLaBaseSeLoActualizaConElDelBackoffice() {
        String mail = "un@mail.com";
        String backofficeId = "1234";
        Usuario usuario = usuarioService.save(Usuario.create("Pepe Sanchez", "pepes", "???", backofficeId, null));

        BackofficeUserTo usuarioBackoffice = BackofficeUserTo.create(backofficeId, mail, "pepes", "Pepe Sanchez", "true", "false", "hmac");
        crearAutenticador().buscarIdDelUsuarioEnLaBase(usuarioBackoffice);

        assertThat(usuarioService.get(usuario.getId()).getMail()).isEqualTo(mail);
    }

    private BackofficeCallbackAuthenticator crearAutenticador() {
        return BackofficeCallbackAuthenticatorForRootsOnly.create(application.injector());
    }


    private void startApplication() {
        application = TestApplication.create(TestConfig.create());
        application.start();
    }
}