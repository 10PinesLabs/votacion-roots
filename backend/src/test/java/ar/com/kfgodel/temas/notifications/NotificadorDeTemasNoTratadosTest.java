package ar.com.kfgodel.temas.notifications;

import ar.com.kfgodel.temas.helpers.ClockMock;
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
import org.simplejavamail.email.Email;
import org.simplejavamail.email.Recipient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class NotificadorDeTemasNoTratadosTest {

    private static final LocalDateTime TIEMPO_DEL_CLOCK = LocalDate.of(2019, 8, 19).atTime(9, 0);
    private static final LocalDate FECHA_DE_REUNION_VALIDA = LocalDate.of(2019, 8, 16);

    private TestHelper helper = new TestHelper();
    private TestApplication application;
    private ReunionService reunionService;
    private MinutaService minutaService;
    private UsuarioService usuarioService;
    private MailerMock mailerMock = new MailerMock();
    private ClockMock clockMock = new ClockMock(TIEMPO_DEL_CLOCK);

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
        notificador.notificar();

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
        notificador.notificar();

        assertThat(mailerMock.getEmailsEnviados()).isEmpty();
    }

    @Test
    public void testElEmailDeNotificacionDeTemasNoTratadosTieneLosDatosCorrectos() {
        Minuta unaMinuta = crearUnaMinutaConUnTemaNoTratado();

        NotificadorDeTemasNoTratados notificador = crearNotificadorDeTemasNoTratados();
        notificador.notificar();

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

    @Test
    public void testSeNotificaElDiaDeSemanaSiguienteALaReunion() {
        LocalDate unViernes = LocalDate.of(2019, 8, 16);
        crearUnaMinutaConUnTemaNoTratadoYFecha(unViernes);
        LocalDate elLunesSiguienteAlViernes = LocalDate.of(2019, 8, 19);
        clockMock.setTiempo(elLunesSiguienteAlViernes.atTime(1, 0));

        NotificadorDeTemasNoTratados notificador = crearNotificadorDeTemasNoTratados();
        notificador.notificar();

        assertThat(mailerMock.cantidadDeEmailsEnviados()).isEqualTo(1);
    }

    @Test
    public void testNoSeNotificaSiNoEsElDiaDeSemanaSiguienteALaReunion() {
        LocalDate unViernes = LocalDate.of(2019, 8, 16);
        crearUnaMinutaConUnTemaNoTratadoYFecha(unViernes);
        LocalDate elLunesSiguienteAlViernes = LocalDate.of(2019, 8, 18);
        clockMock.setTiempo(elLunesSiguienteAlViernes.atTime(8, 0));

        NotificadorDeTemasNoTratados notificador = crearNotificadorDeTemasNoTratados();
        notificador.notificar();

        assertThat(mailerMock.cantidadDeEmailsEnviados()).isEqualTo(0);
    }

    @Test
    public void testNoSeVuelvenANotificarLosTemasQueYaFueronNotificados() {
        crearUnaMinutaConUnTemaNoTratado();

        NotificadorDeTemasNoTratados notificador = crearNotificadorDeTemasNoTratados();
        notificador.notificar();
        notificador.notificar();

        assertThat(mailerMock.cantidadDeEmailsEnviados()).isEqualTo(1);
    }

    @Test
    public void testNoSeNotificaSiNoHayMinutas() {
        NotificadorDeTemasNoTratados notificador = crearNotificadorDeTemasNoTratados();
        notificador.notificar();

        assertThat(mailerMock.cantidadDeEmailsEnviados()).isEqualTo(0);
    }

    private Minuta crearUnaMinutaConUnTemaNoTratadoYFecha(LocalDate unaFecha) {
        Usuario unUsuario = usuarioService.save(helper.unUsuario());
        return crearUnaMinutaConTemasNoTratadosDeYFecha(Collections.singletonList(unUsuario), unaFecha);
    }

    private Minuta crearUnaMinutaConTemasNoTratadosDeYFecha(Collection<Usuario> unosAutores, LocalDate unaFecha) {
        List<TemaDeReunion> unosTemas = unosAutores.stream().map(helper::unTemaDeReunionDe).collect(Collectors.toList());
        Reunion unaReunion = helper.unaReunionMinuteadaConTemas(unosTemas);
        unaReunion.setFecha(unaFecha);
        Minuta unaMinuta = Minuta.create(reunionService.save(unaReunion));
        return minutaService.save(unaMinuta);
    }

    private Minuta crearUnaMinutaConUnTemaNoTratado() {
        return crearUnaMinutaConUnTemaNoTratadoYFecha(FECHA_DE_REUNION_VALIDA);
    }

    private void crearUnaMinutaConTemasNoTratadosDe(Collection<Usuario> unosAutores) {
        crearUnaMinutaConTemasNoTratadosDeYFecha(unosAutores, FECHA_DE_REUNION_VALIDA);
    }

    private void crearUnaMinutaConTemasTratados() {
        Reunion unaReunion = helper.unaReunionMinuteadaConTemas(Collections.singletonList(helper.unTemaDeReunion()));
        unaReunion.setFecha(FECHA_DE_REUNION_VALIDA);
        Minuta unaMinuta = Minuta.create(reunionService.save(unaReunion));
        unaMinuta.getTemas().forEach(temaDeMinuta -> temaDeMinuta.setFueTratado(true));
        minutaService.save(unaMinuta);
    }

    private NotificadorDeTemasNoTratados crearNotificadorDeTemasNoTratados() {
        return NotificadorDeTemasNoTratados.create(application.injector(), clockMock.getClock(), mailerMock.getMailer());
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
