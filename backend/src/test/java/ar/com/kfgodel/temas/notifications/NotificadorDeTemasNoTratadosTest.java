package ar.com.kfgodel.temas.notifications;

import ar.com.kfgodel.temas.helpers.MailerMock;
import ar.com.kfgodel.temas.helpers.TestConfig;
import ar.com.kfgodel.temas.helpers.TestHelper;
import ar.com.kfgodel.temas.persistence.TestApplication;
import convention.persistent.*;
import convention.rest.api.MinutaResource;
import convention.services.MinutaService;
import convention.services.ReunionService;
import convention.services.UsuarioService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.Recipient;
import org.simplejavamail.mailer.Mailer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.mock;

public class NotificadorDeTemasNoTratadosTest {

    private TestHelper helper = new TestHelper();
    private TestApplication application;
    private ReunionService reunionService;
    private MinutaService minutaService;
    private UsuarioService usuarioService;
    private MailerMock mailerMock = new MailerMock();

    @Before
    public void setUp() {
        iniciarAplicacion();
        obtenerServices();
    }

    @After
    public void tearDown() {
        application.clearServices();
    }

    @Test
    public void testSeNotificaALosAutoresDeLosTemasQueNoFueronTratados() {
        List<Usuario> unosUsuarios = Arrays.asList(
                usuarioService.save(helper.unUsuario()),
                usuarioService.save(helper.otroUsuario()));

        crearUnaMinutaConTemasNoTratadosDe(unosUsuarios);

        NotificadorDeTemasNoTratados notificador = crearNotificadorDeTemasNoTratados();
        notificador.run();

        List<String> destinatariosEsperados = unosUsuarios.stream().map(Usuario::getMail).collect(Collectors.toList());
        List<String> destinatarios = mailerMock.getEmailsEnviados().stream()
                .flatMap(email -> email.getRecipients().stream().map(Recipient::getAddress))
                .collect(Collectors.toList());
        assertThat(destinatarios).containsExactlyElementsOf(destinatariosEsperados);
    }

    @Test
    public void testNoSeNotificaALosAutoresDeLosTemasQueFueronTratados() {
        crearUnaMinutaConTemasTratados();

        NotificadorDeTemasNoTratados notificador = crearNotificadorDeTemasNoTratados();
        notificador.run();

        assertThat(mailerMock.getEmailsEnviados()).isEmpty();
    }

    @Test
    public void testElEmailDeNotificacionDeTemasNoTratadosTieneLosDatosCorrectos() {
        Minuta unaMinuta = crearUnaMinutaConUnTemaNoTratado();

        NotificadorDeTemasNoTratados notificador = crearNotificadorDeTemasNoTratados();
        notificador.run();

        Email emailEnviado = mailerMock.getEmailsEnviados().get(0);
        Recipient remitente = emailEnviado.getFromRecipient();
        Recipient destinatario = emailEnviado.getRecipients().get(0);
        TemaDeReunion temaNoTratado = unaMinuta.getTemas().get(0).getTema();
        String tituloDelTema = temaNoTratado.getTitulo();
        Usuario autorDelTema = temaNoTratado.getAutor();
        assertThat(destinatario.getName()).isEqualTo(autorDelTema.getName());
        assertThat(destinatario.getAddress()).isEqualTo(autorDelTema.getMail());
        assertThat(remitente.getName()).isEqualTo("Aviso Tema No Propuesto");
        assertThat(remitente.getAddress()).isEqualTo(MailerConfiguration.getSenderAdress());
        assertThat(emailEnviado.getSubject()).isEqualTo(String.format("Tu tema \"%s\" no fue tratado", tituloDelTema));
        assertThat(emailEnviado.getPlainText()).isEqualTo(
                String.format("Hola! El tema \"%s\" que presentaste en la roots pasada no fue tratado. Sentite libre de volver a proponerlo!",
                        tituloDelTema));
    }

    private Minuta crearUnaMinutaConUnTemaNoTratado() {
        Usuario unUsuario = usuarioService.save(helper.unUsuario());
        return crearUnaMinutaConTemasNoTratadosDe(Collections.singletonList(unUsuario));
    }

    private Minuta crearUnaMinutaConTemasNoTratadosDe(List<Usuario> unosUsuarios) {
        List<TemaDeReunion> unosTemas = unosUsuarios.stream()
                .map(helper::unTemaDeReunionDe).collect(Collectors.toList());
        Reunion unaReunion = helper.unaReunionMinuteadaConTemas(unosTemas);
        Minuta unaMinuta = Minuta.create(reunionService.save(unaReunion));
        return minutaService.save(unaMinuta);
    }

    private void crearUnaMinutaConTemasTratados() {
        Reunion unaReunion = helper.unaReunionMinuteadaConTemas(Collections.singletonList(helper.unTemaDeReunion()));
        Minuta unaMinuta = Minuta.create(reunionService.save(unaReunion));
        unaMinuta.getTemas().forEach(temaDeMinuta -> temaDeMinuta.setFueTratado(true));
        minutaService.save(unaMinuta);
    }

    private NotificadorDeTemasNoTratados crearNotificadorDeTemasNoTratados() {
        return NotificadorDeTemasNoTratados.create(application.injector(), mailerMock.getMailer());
    }

    private void iniciarAplicacion() {
        application = TestApplication.create(TestConfig.create());
        application.start();
        application.injector().createInjected(MinutaResource.class);
    }

    private void obtenerServices() {
        reunionService = application.getImplementationFor(ReunionService.class);
        minutaService = application.getImplementationFor(MinutaService.class);
        usuarioService = application.getImplementationFor(UsuarioService.class);
    }
}
