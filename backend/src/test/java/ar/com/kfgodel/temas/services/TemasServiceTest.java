package ar.com.kfgodel.temas.services;

import ar.com.kfgodel.appbyconvention.operation.api.ApplicationOperation;
import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.orm.api.operations.basic.Save;
import ar.com.kfgodel.temas.acciones.CalculadorDeProximaFecha;
import ar.com.kfgodel.temas.apiRest.JettyIdentityAdapterTest;
import ar.com.kfgodel.temas.apiRest.SecurityContextTest;
import ar.com.kfgodel.temas.application.Application;
import ar.com.kfgodel.temas.helpers.TestConfig;
import ar.com.kfgodel.temas.persistence.TestApplication;
import convention.persistent.*;
import convention.rest.api.DuracionesResource;
import convention.rest.api.ReunionResource;
import convention.rest.api.TemaDeReunionResource;
import convention.rest.api.TemaGeneralResource;
import convention.rest.api.tos.ReunionTo;
import convention.rest.api.tos.TemaDeReunionTo;
import convention.services.ReunionService;
import convention.services.TemaService;
import convention.services.UsuarioService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.SecurityContext;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;


/**
 * Created by fede on 22/06/17.
 */
public class TemasServiceTest {

    TestApplication app;

    ReunionService reunionService;
    UsuarioService usuarioService;
    TemaService temaService;
    ReunionResource reunionResource;
    DuracionesResource duracionResource;
    SecurityContext testContextUserFeche;
    Long userId;
    Long otherUserId;
    Usuario otherUser;
    Usuario user;
    private TemaDeReunionResource temaDeReunionResource;
    private TemaGeneralResource temaGeneralResource;

    @Before
    public void setUp() {
        app = (TestApplication) TestApplication.create(TestConfig.create());
        app.start();
        DependencyInjector injector = app.injector();
        temaDeReunionResource = TemaDeReunionResource.create(injector);
        duracionResource = DuracionesResource.create(injector);
        temaGeneralResource = TemaGeneralResource.create(injector);

        reunionResource = ReunionResource.create(injector);
        temaService = injector.getImplementationFor(TemaService.class).get();
        usuarioService = injector.createInjected(UsuarioService.class);
        reunionService = injector.getImplementationFor(ReunionService.class).get();

        testContextUserFeche = new SecurityContextTest(usuarioService.getAll().get(0).getId());

        userId = ((JettyIdentityAdapterTest) testContextUserFeche.getUserPrincipal()).getApplicationIdentification();
        user = usuarioService.get(userId);
        otherUser = usuarioService.getAll().stream().filter(userTo -> !userTo.getId().equals(userId)).findFirst().get();
        otherUserId = otherUser.getId();
    }

    @After
    public void drop() {
        app.clearServices();
    }


    @Test
    public void alTraerUnaReunionConLaSeccionDeUnUsuarioNoTraeLosVotosDeOtrosSiLaReunionEstaPendiente() {

        Reunion reunion = new Reunion();
        TemaDeReunion temaDeLaReunion = TemaDeReunionConDescripcion.create();

        reunion = reunionService.save(reunion);

        Usuario unUsuario = usuarioService.get(userId);
        Usuario otroUsuario = usuarioService.get(otherUserId);

        temaDeLaReunion.setReunion(reunion);
        temaDeLaReunion.setAutor(unUsuario);
        temaDeLaReunion.setInteresados(Arrays.asList(unUsuario, otroUsuario, otroUsuario));

        reunion.setTemasPropuestos(Arrays.asList(temaDeLaReunion));

        reunion = reunionService.update(reunion);

        ReunionTo reunionSolicitada = reunionResource.getSingle(reunion.getId(), testContextUserFeche);

        TemaDeReunionTo tema = reunionSolicitada.getTemasPropuestos().get(0);
        Assert.assertEquals(tema.getIdsDeInteresados().size(), 1);
    }

    @Test
    public void alTraerUnaReunionConLaSeccionDeUnUsuarioNoTraeTodosLosVotosSiLaReunionEstaCerrada() {
        Reunion reunion = new Reunion();
        TemaDeReunion temaDeLaReunion = new TemaDeReunionConDescripcion();
        reunion.setStatus(StatusDeReunion.CERRADA);
        reunion = reunionService.save(reunion);

        Usuario unUsuario = usuarioService.get(userId);
        Usuario otroUsuario = usuarioService.get(otherUserId);

        temaDeLaReunion.setReunion(reunion);
        temaDeLaReunion.setAutor(unUsuario);
        temaDeLaReunion.setInteresados(Arrays.asList(unUsuario, otroUsuario, otroUsuario));

        reunion.setTemasPropuestos(Arrays.asList(temaDeLaReunion));

        reunion = reunionService.update(reunion);

        ReunionTo reunionSolicitada = reunionResource.getSingle(reunion.getId(), testContextUserFeche);

        TemaDeReunionTo tema = reunionSolicitada.getTemasPropuestos().get(0);
        Assert.assertEquals(tema.getIdsDeInteresados().size(), 3);
    }

    @Test
    public void sePersistenCorrectamenteLasDuracionesDeLosTemas() {
        TemaDeReunion temaDeLaReunion = TemaDeReunionConDescripcion.create();
        temaDeLaReunion.setDuracion(DuracionDeTema.CORTO);
        temaDeLaReunion = ApplicationOperation.createFor(app.injector())
                .insideATransaction()
                .taking(temaDeLaReunion)
                .convertingTo(TemaDeReunion.class)
                .applyingResultOf(Save::create)
                .convertTo(TemaDeReunion.class);
        Assert.assertEquals(temaDeLaReunion.getDuracion(), DuracionDeTema.CORTO);
    }

    @Test
    public void crearProximaReunionDevuelveElTercerViernesDeEsteMesSiNoPasoTodavia() {
        LocalDate diaDeHoy = LocalDate.of(2017, 6, 1);
        LocalDate proximaRoot = new CalculadorDeProximaFecha().
                calcularFechaDeRoots(diaDeHoy);
        Assert.assertEquals(proximaRoot, LocalDate.of(2017, 6, 16));
    }

    @Test
    public void crearProximaReunionDevuelveElTercerViernesDelProximoMesSiYaPasoElTercerViernesDeEsteMes() {
        LocalDate diaDeHoy = LocalDate.of(2017, 6, 27);
        LocalDate proximaRoot = new CalculadorDeProximaFecha().
                calcularFechaDeRoots(diaDeHoy);
        Assert.assertEquals(proximaRoot, LocalDate.of(2017, 7, 21));
    }

    @Test
    public void crearProximaReunionDevuelveHoySiEsElTercerViernesDelMes() {
        LocalDate diaDeHoy = LocalDate.of(2017, 6, 16);
        LocalDate proximaRoot = new CalculadorDeProximaFecha().
                calcularFechaDeRoots(diaDeHoy);
        Assert.assertEquals(proximaRoot, diaDeHoy);

    }

    @Test
    public void votarUnTemaNoLoVuelveACrearPeroSiGuardaLosVotos() {
        TemaDeReunion unTema = TemaDeReunionConDescripcion.create();
        unTema.setDuracion(DuracionDeTema.MEDIO);
        unTema = temaService.save(unTema);
        Assert.assertEquals(temaService.getAll().size(), 1);
        temaService.updateAndMapping(unTema.getId(), temaDeReunion -> {
            temaDeReunion.agregarInteresado(otherUser);

            return temaDeReunion;
        });
        Assert.assertEquals(temaService.getAll().size(), 1);
        temaService.updateAndMapping(unTema.getId(), temaDeReunion -> {

            temaDeReunion.agregarInteresado(otherUser);
            return temaDeReunion;
        });
        Assert.assertEquals(temaService.getAll().size(), 1);
        Assert.assertEquals(temaService.get(unTema.getId()).getInteresados().size(), 2);
    }

    @Test
    public void sePuedeAgregarUnVotoEnUnTemaConMenosDe3VotosDelUsuario() {
        TemaDeReunion unTema = temaService.save(TemaDeReunionConDescripcion.create());
        temaService.updateAndMapping(unTema.getId(), temaDeReunion -> new TemaDeReunionResource().votarTema(otherUser, temaDeReunion));
        Assert.assertEquals(temaService.get(unTema.getId()).getInteresados().size(), 1);
    }

    @Test(expected = WebApplicationException.class)
    public void NosePuedeAgregarUnVotoEnUnTemaConMasDe3VotosDelUsuario() {
        TemaDeReunion unTema = temaService.save(TemaDeReunionConDescripcion.create());
        temaService.updateAndMapping(unTema.getId(), temaDeReunion -> new TemaDeReunionResource().votarTema(otherUser, temaDeReunion));
        temaService.updateAndMapping(unTema.getId(), temaDeReunion -> new TemaDeReunionResource().votarTema(otherUser, temaDeReunion));
        temaService.updateAndMapping(unTema.getId(), temaDeReunion -> new TemaDeReunionResource().votarTema(otherUser, temaDeReunion));
        temaService.updateAndMapping(unTema.getId(), temaDeReunion -> new TemaDeReunionResource().votarTema(otherUser, temaDeReunion));
    }

    @Test
    public void sePuedeQuitarUnVotoEnUnTemaConVotosDelUsuario() {
        TemaDeReunion unTema = temaService.save(TemaDeReunionConDescripcion.create());
        temaService.updateAndMapping(unTema.getId(), temaDeReunion -> new TemaDeReunionResource().votarTema(otherUser, temaDeReunion));
        temaService.updateAndMapping(unTema.getId(), temaDeReunion -> new TemaDeReunionResource().desvotarTema(otherUser, temaDeReunion));

        Assert.assertEquals(temaService.get(unTema.getId()).getInteresados().size(), 0);
    }

    @Test(expected = WebApplicationException.class)
    public void NoSePuedeQuitarUnVotoEnUnTemaSinVotosDelUsuario() {
        TemaDeReunion unTema = temaService.save(TemaDeReunionConDescripcion.create());
        temaService.updateAndMapping(unTema.getId(), temaDeReunion -> new TemaDeReunionResource().desvotarTema(otherUser, temaDeReunion));


    }

    @Test
    public void siUnaReunionEstaCerradaSeRecuperaConOrdenDePrioridad() {
        TemaDeReunion temaDeMayorPrioridad = TemaDeReunionConDescripcion.create();
        temaDeMayorPrioridad.setPrioridad(1);
        TemaDeReunion temaDeMenorPrioridad = TemaDeReunionConDescripcion.create();
        temaDeMenorPrioridad.setPrioridad(4);
        TemaDeReunion temaDePrioridadMedia = TemaDeReunionConDescripcion.create();
        temaDePrioridadMedia.setPrioridad(2);
        Reunion unaReunion = Reunion.create(LocalDate.now());
        unaReunion.setTemasPropuestos(Arrays.asList(temaDeMenorPrioridad, temaDeMayorPrioridad, temaDePrioridadMedia));
        unaReunion.setStatus(StatusDeReunion.CERRADA);
        unaReunion = reunionService.save(unaReunion);
        temaDeMayorPrioridad.setReunion(unaReunion);
        temaDeMenorPrioridad.setReunion(unaReunion);
        temaDePrioridadMedia.setReunion(unaReunion);
        unaReunion = reunionService.update(unaReunion);
        unaReunion = reunionService.getAndMapping(unaReunion.getId(), reunion -> reunionResource.muestreoDeReunion(reunion, userId, testContextUserFeche));

        Assert.assertArrayEquals(unaReunion.getTemasPropuestos().toArray(), Arrays.asList(temaDeMayorPrioridad, temaDePrioridadMedia, temaDeMenorPrioridad).toArray());
    }

    @Test
    public void lasDuracionesDeTemasSeDevuelvenOrdenadasEnElResource() {
        List<DuracionDeTema> listaOrdenada = Arrays.asList(DuracionDeTema.values());
        listaOrdenada.sort((duracion1, duracion2) -> duracion1.getCantidadDeMinutos() - duracion2.getCantidadDeMinutos());
        Assert.assertArrayEquals(duracionResource.getAllDuraciones().toArray(), listaOrdenada.toArray());
    }

    @Test
    public void seObtieneCorrectamenteLaObligatoriedadDeUnTema() {
        TemaDeReunion temaDeLaReunion = TemaDeReunionConDescripcion.create();
        temaDeLaReunion.setObligatoriedad(ObligatoriedadDeTema.OBLIGATORIO);

        temaDeLaReunion = temaService.save(temaDeLaReunion);

        TemaDeReunionTo temaPersistido = temaDeReunionResource.getSingle(temaDeLaReunion.getId());
        Assert.assertEquals("OBLIGATORIO", temaPersistido.getObligatoriedad());
    }

    @Test
    public void alObtenerUnaReunionSusTemasTienenObligatoriedad() {
        Reunion reunion = new Reunion();
        reunion = reunionService.save(reunion);

        TemaDeReunion tema = TemaDeReunionConDescripcion.create();
        tema.setReunion(reunion);
        tema.setObligatoriedad(ObligatoriedadDeTema.OBLIGATORIO);
        tema.setDescripcion("una descripción");
        temaService.save(tema);

        ReunionTo reunionRecuperada = reunionResource.getSingle(reunion.getId(), testContextUserFeche);
        Assert.assertEquals("OBLIGATORIO", reunionRecuperada.getTemasPropuestos().get(0).getObligatoriedad());
    }

    @Test
    public void alRecibirUnTemaDelFrontendSeRecibeSuMomentoDeCreacion() {
        TemaDeReunion tema = new TemaDeReunionConDescripcion();
        tema = temaService.save(tema);

        TemaDeReunionTo temaEnviado = temaDeReunionResource.getSingle(tema.getId());

        TemaDeReunion temaRecibido = ApplicationOperation.createFor(app.injector())
                .insideATransaction()
                .taking(temaEnviado)
                .convertingTo(TemaDeReunion.class)
                .convertTo(TemaDeReunion.class);

        Assert.assertFalse(temaRecibido.getMomentoDeCreacion() == null);
    }

    @Test
    public void alRecibirUnaReunionDelFrontendSusTemasTienenMomentoDeCreacion() {
        Reunion reunion = new Reunion();
        TemaDeReunion tema = TemaDeReunionConDescripcion.create();
        tema.setReunion(reunion);
        reunion = reunionService.save(reunion);
        temaService.save(tema);

        ReunionTo reunionEnviada = reunionResource.getSingle(reunion.getId(), testContextUserFeche);

        Reunion reunionRecibida = ApplicationOperation.createFor(app.injector())
                .insideATransaction()
                .taking(reunionEnviada)
                .convertingTo(Reunion.class)
                .convertTo(Reunion.class);

        Assert.assertFalse(reunionRecibida.getTemasPropuestos().get(0).getMomentoDeCreacion() == null);
    }
}

