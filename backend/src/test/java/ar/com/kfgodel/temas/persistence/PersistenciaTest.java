package ar.com.kfgodel.temas.persistence;

import ar.com.kfgodel.temas.application.Application;
import ar.com.kfgodel.temas.filters.reuniones.AllReunionesUltimaPrimero;
import ar.com.kfgodel.temas.helpers.TestConfig;
import com.mchange.net.MailSender;
import convention.persistent.*;
import convention.rest.api.ReunionResource;
import convention.services.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

/**
 * Created by sandro on 19/06/17.
 */
public class PersistenciaTest {

    private Application application;
    private ReunionService reunionService;
    private TemaService temaService;
    private TemaGeneralService temaGeneralService;
    private MinutaService minutaService;
    private UsuarioService usuarioService;
    private TemaDeMinutaService temaDeMinutaService;

    @Before
    public void setUp(){
        startApplication();
       temaService = application.injector().createInjected(TemaService.class);
        temaGeneralService = application.injector().createInjected(TemaGeneralService.class);
        application.injector().createInjected(ReunionResource.class);
        reunionService = application.injector().getImplementationFor(ReunionService.class).get();
        minutaService = application.injector().createInjected(MinutaService.class);
        usuarioService = application.injector().createInjected(UsuarioService.class);

        temaDeMinutaService=application.injector().createInjected(TemaDeMinutaService.class);


        application.injector().bindTo(TemaDeMinutaService.class, temaDeMinutaService);
        application.injector().bindTo(ReunionService.class, reunionService);
        application.injector().bindTo(TemaService.class, temaService);
        application.injector().bindTo(TemaGeneralService.class, temaGeneralService);
        application.injector().bindTo(MinutaService.class, minutaService);
        application.injector().bindTo(UsuarioService.class, usuarioService);
        application.injector().bindTo(TemaDeMinutaService.class, temaDeMinutaService);
    }
    @After
    public void drop(){
        application.stop();
    }
    @Test
    public void test01SePuedePersistirCorrectamenteUnaReunion(){
        Reunion nuevaReunion = new Reunion();

        int cantidadDeReunionesAnteriores = reunionService.getAll(AllReunionesUltimaPrimero.create()).size();

        reunionService.save(nuevaReunion);

        List<Reunion> reunionesPersistidas = reunionService.getAll(AllReunionesUltimaPrimero.create());

        Assert.assertEquals(cantidadDeReunionesAnteriores + 1, reunionesPersistidas.size());
    }

    @Test
    public void test02SePuedePersistirCorrectamenteUnTema(){
        TemaDeReunion nuevoTema = TemaDeReunion.create();

        int cantidadDeTemasAnteriores = temaService.getAll().size();

        temaService.save(nuevoTema);

        List<TemaDeReunion> temasPersistidos = temaService.getAll();

        Assert.assertEquals(cantidadDeTemasAnteriores + 1, temasPersistidos.size());
    }

    @Test
    public void test03AlObtenerUnaReunionSeTraenSoloSusTemas(){
       Reunion reunion = new Reunion();
       TemaDeReunion temaDeLaReunion = TemaDeReunion.create();
       TemaDeReunion temaDeOtraReunion =TemaDeReunion.create();
            temaService.save(temaDeOtraReunion);
       reunion = reunionService.save(reunion);

       temaDeLaReunion.setReunion(reunion);


       reunion.setTemasPropuestos(Arrays.asList(temaDeLaReunion));

       reunion = reunionService.update(reunion);


       Reunion reunionPersistida = reunionService.get(reunion.getId());

       Assert.assertEquals(1, reunionPersistida.getTemasPropuestos().size());

    }

    @Test
    public void test04LaObligatoriedadDeUnTemaSePersisteCorrectamente(){
        TemaDeReunion tema =TemaDeReunion.create();
        tema.setObligatoriedad(ObligatoriedadDeTema.OBLIGATORIO);

        tema = temaService.save(tema);

        TemaDeReunion temaPersistido = temaService.get(tema.getId());
        Assert.assertEquals(ObligatoriedadDeTema.OBLIGATORIO, temaPersistido.getObligatoriedad());
    }

    @Test
    public void test05ElMomentoDeCreacionDeUnTemaSeCreaAlPersistirElTema(){
        TemaDeReunion tema = new TemaDeReunion();
        tema = temaService.save(tema);
        TemaDeReunion temaPersistido = temaService.get(tema.getId());
        Assert.assertFalse(temaPersistido.getMomentoDeCreacion() == null);
    }

    @Test
    public void test06SePuedePersistirCorrectamenteUnTemaGeneral(){
        TemaGeneral temaGeneral = new TemaGeneral();
        String titulo = "Tema General";
        temaGeneral.setTitulo(titulo);
        temaGeneral = temaGeneralService.save(temaGeneral);
        Assert.assertEquals(titulo, temaGeneralService.get(temaGeneral.getId()).getTitulo());
    }

    @Test
    public void test07AlGuardarUnaReunionSeAgreganLosTemasGeneralesCorrespondientes(){
        TemaGeneral temaGeneral = new TemaGeneral();
        temaGeneralService.save(temaGeneral);
        Reunion reunion = Reunion.create(LocalDate.of(2017, 06, 26));

        reunion = reunionService.save(reunion);

        Assert.assertEquals(1, reunionService.get(reunion.getId()).getTemasPropuestos().size());
    }

    @Test
    public void test08AlGuardarUnTemaGeneralElMismoSeAgregaATodasLasReunionesAbiertas(){
        Reunion reunionAbierta = Reunion.create(LocalDate.of(2017, 06, 26));
        reunionAbierta = reunionService.save(reunionAbierta);

        TemaGeneral temaGeneral = new TemaGeneral();
        temaGeneralService.save(temaGeneral);

        Assert.assertEquals(1, reunionService.get(reunionAbierta.getId()).getTemasPropuestos().size());
    }

    @Test
    public void test09AlGuardarUnaTemaGeneralElMismoNoSeAgregaALasReunionesCerradas(){
        Reunion reunionCerrada = Reunion.create(LocalDate.of(2017, 06, 26));
        reunionCerrada.setStatus(StatusDeReunion.CERRADA);
        reunionCerrada = reunionService.save(reunionCerrada);

        TemaGeneral temaGeneral = new TemaGeneral();
        temaGeneralService.save(temaGeneral);

        Assert.assertEquals(0, reunionService.get(reunionCerrada.getId()).getTemasPropuestos().size());
    }

    @Test
    public void test10SePuedePersistirSiUnTemaDeReunionFueGeneradoPorUnTemaGeneral(){
        Reunion reunion = new Reunion();
        reunionService.save(reunion);
        TemaGeneral temaGeneral = new TemaGeneral();
        temaGeneralService.save(temaGeneral);
        TemaDeReunion temaDeReunion = temaGeneral.generarTemaPara(reunion);
        temaDeReunion = temaService.save(temaDeReunion);

        Assert.assertTrue(temaService.get(temaDeReunion.getId()).fueGeneradoPorUnTemaGeneral());
    }

    @Test
    public void test11SePuedePersistirUnaMinuta(){
        Reunion reunion = Reunion.create(LocalDate.of(2017, 06, 26));
        TemaDeReunion unTema = TemaDeReunion.create();
        reunion = reunionService.save(reunion);
        unTema.setReunion(reunion);
        temaService.save(unTema);
        reunion.setTemasPropuestos(Arrays.asList(unTema));
        reunion = reunionService.update(reunion);

        Minuta minuta = Minuta.create(reunion);
        minuta = minutaService.save(minuta);

        Minuta minutaRecuperada = minutaService.get(minuta.getId());

        Assert.assertEquals(minuta.getId(), minutaRecuperada.getId());
        Assert.assertEquals(minuta.getReunion().getId(), minutaRecuperada.getReunion().getId());
        Assert.assertEquals(minuta.getFecha(), minutaRecuperada.getFecha());
        Assert.assertEquals(1, minutaRecuperada.getTemas().size());
    }

    @Test
    public void test12AlBorrarUnTemaGeneralSusTemasDeReunionAsociadosSeEliminanTambien(){
        Reunion reunion = Reunion.create(LocalDate.of(2017, 06, 26));
        TemaGeneral temaGeneral = new TemaGeneral();

        reunion = reunionService.save(reunion);
        temaGeneral = temaGeneralService.save(temaGeneral);

        Assert.assertEquals(1, reunionService.get(reunion.getId()).getTemasPropuestos().size());

        temaGeneralService.delete(temaGeneral.getId());

        Assert.assertEquals(0, reunionService.get(reunion.getId()).getTemasPropuestos().size());
    }

    @Test
    public void test13AlBorrarUnTemaGeneralSusTemasDeReunionAsociadosNoSeEliminanDeLasReunionesCerradas(){
        Reunion reunion = Reunion.create(LocalDate.of(2017, 06, 26));
        TemaGeneral temaGeneral = new TemaGeneral();

        reunion = reunionService.save(reunion);
        temaGeneral = temaGeneralService.save(temaGeneral);

        reunion = reunionService.get(reunion.getId());
        reunion.setStatus(StatusDeReunion.CERRADA);
        reunionService.update(reunion);

        temaGeneralService.delete(temaGeneral.getId());
        Assert.assertEquals(1, reunionService.get(reunion.getId()).getTemasPropuestos().size());
    }

    @Test
    public void test14AlBorrarUnTemaGeneralNoSeBorranlosTemasDeReunionQueNoFueronGeneradosPorEl(){
        Reunion reunion = Reunion.create(LocalDate.of(2017, 06, 26));
        TemaDeReunion unTemaNoGenerado = TemaDeReunion.create();
        unTemaNoGenerado.setReunion(reunion);
        reunion.setTemasPropuestos(Arrays.asList(unTemaNoGenerado));

        reunion = reunionService.save(reunion);
        temaService.save(unTemaNoGenerado);

        TemaGeneral unTemaGeneral = new TemaGeneral();
        unTemaGeneral = temaGeneralService.save(unTemaGeneral);
        TemaGeneral otroTemaGeneral = new TemaGeneral();
        temaGeneralService.save(otroTemaGeneral);

        temaGeneralService.delete(unTemaGeneral.getId());
        Assert.assertEquals(2, reunionService.get(reunion.getId()).getTemasPropuestos().size());
    }

    @Test
    public void test15AlEditarUnTemaGeneralSusTemasDeReunionAsociadosSeModificanTambien(){
        Reunion reunion = Reunion.create(LocalDate.of(2017, 06, 26));
        TemaGeneral temaGeneral = new TemaGeneral();
        temaGeneral.setTitulo("Título");

        reunion = reunionService.save(reunion);
        temaGeneral = temaGeneralService.save(temaGeneral);

        temaGeneral.setTitulo("Título modificado");
        temaGeneralService.update(temaGeneral);

        Assert.assertEquals("Título modificado", reunionService.get(reunion.getId()).getTemasPropuestos().get(0).getTitulo());
    }

    @Test
    public void test16AlEditarUnTemaGeneralSusTemasDeReunionAsociadosNoSeModificanEnLasReunionesCerradas(){
        Reunion reunion = Reunion.create(LocalDate.of(2017, 06, 26));
        TemaGeneral temaGeneral = new TemaGeneral();
        temaGeneral.setTitulo("Título");

        reunion = reunionService.save(reunion);
        temaGeneral = temaGeneralService.save(temaGeneral);

        reunion = reunionService.get(reunion.getId());
        reunion.setStatus(StatusDeReunion.CERRADA);
        reunionService.update(reunion);

        temaGeneral.setTitulo("Título modificado");
        temaGeneralService.update(temaGeneral);
        Assert.assertEquals("Título", reunionService.get(reunion.getId()).getTemasPropuestos().get(0).getTitulo());
    }

    @Test
    public void test17AlEditarUnTemaGeneralNoSeModificanlosTemasDeReunionQueNoFueronGeneradosPorEl(){
        Reunion reunion = Reunion.create(LocalDate.of(2017, 06, 26));
        TemaDeReunion unTemaNoGenerado = TemaDeReunion.create();
        unTemaNoGenerado.setReunion(reunion);
        unTemaNoGenerado.setTitulo("Título");
        reunion.setTemasPropuestos(Arrays.asList(unTemaNoGenerado));

        reunion = reunionService.save(reunion);
        temaService.save(unTemaNoGenerado);

        TemaGeneral unTemaGeneral = new TemaGeneral();
        unTemaGeneral.setTitulo("Título 2");
        unTemaGeneral = temaGeneralService.save(unTemaGeneral);
        TemaGeneral otroTemaGeneral = new TemaGeneral();
        otroTemaGeneral.setTitulo("Título 3");
        temaGeneralService.save(otroTemaGeneral);

        unTemaGeneral.setTitulo("Título modificado");
        temaGeneralService.update(unTemaGeneral);
        Assert.assertEquals("Título", reunionService.get(reunion.getId()).getTemasPropuestos().get(0).getTitulo());
        Assert.assertEquals("Título modificado", reunionService.get(reunion.getId()).getTemasPropuestos().get(1).getTitulo());
        Assert.assertEquals("Título 3", reunionService.get(reunion.getId()).getTemasPropuestos().get(2).getTitulo());
    }

    @Test
    public void test18AlGuardarUnTemaGeneralLosTemasDeReunionGeneradosTienenComoUltimoModificadorAlDelTemaGeneral(){
        Reunion reunion = Reunion.create(LocalDate.of(2017, 06, 26));
        TemaGeneral temaGeneral = new TemaGeneral();

        Usuario user = new Usuario();
        user.setName("Sandro");
        usuarioService.save(user);
        temaGeneral.setUltimoModificador(user);

        reunion = reunionService.save(reunion);
        temaGeneralService.save(temaGeneral);

        Assert.assertEquals("Sandro", reunionService.get(reunion.getId()).getTemasPropuestos().get(0).getUltimoModificador().getName());
    }

    @Test
    public void test19AlEditarUnTemaGeneralSeActualizaElUltimoModificadorDeSusTemasDeRerunionAsociados(){
        Reunion reunion = Reunion.create(LocalDate.of(2017, 06, 26));
        TemaGeneral temaGeneral = new TemaGeneral();

        reunion = reunionService.save(reunion);
        temaGeneral = temaGeneralService.save(temaGeneral);

        Usuario user = new Usuario();
        user.setName("Sandro");
        usuarioService.save(user);

        temaGeneral.setUltimoModificador(user);
        temaGeneralService.update(temaGeneral);

        Assert.assertEquals("Sandro", reunionService.get(reunion.getId()).getTemasPropuestos().get(0).getUltimoModificador().getName());
    }

    @Test
    public void test20SePuedePersistirSiUnTemaDeMinutaFueTratado(){
        TemaDeMinuta temaDeMinuta = new TemaDeMinuta();
        temaDeMinuta.setFueTratado(true);
        temaDeMinuta = temaDeMinutaService.save(temaDeMinuta);
        Assert.assertTrue(temaDeMinutaService.get(temaDeMinuta.getId()).getFueTratado());
    }
    @Test
    public void test20UnTemaDeMinutaSePuedePersistirConActionItems(){
        TemaDeMinuta temaDeMinuta=new TemaDeMinuta();
        temaDeMinuta.setConclusion("una conclusion");
        ActionItem unActionItem=new ActionItem();
        unActionItem.setDescripcion("unActionItem");
        temaDeMinuta.setActionItems(Arrays.asList(unActionItem));
        temaDeMinuta=temaDeMinutaService.save(temaDeMinuta);
        temaDeMinuta=temaDeMinutaService.get(temaDeMinuta.getId());


        Assert.assertEquals(temaDeMinuta.getActionItems().size(),1);

    }
    @Test
    public void test21UnTemaDeMinutaPuedeUpdatearSusActionItems(){
        TemaDeMinuta temaDeMinuta=new TemaDeMinuta();
        temaDeMinuta.setConclusion("una conclusion");
        ActionItem unActionItem=new ActionItem();
        unActionItem.setDescripcion("unActionItem");
        temaDeMinuta.setActionItems(Arrays.asList(unActionItem));
        temaDeMinuta=temaDeMinutaService.save(temaDeMinuta);
        unActionItem.setDescripcion("cambie");
        temaDeMinutaService.update(temaDeMinuta);
        temaDeMinuta=temaDeMinutaService.get(temaDeMinuta.getId());


        Assert.assertEquals(temaDeMinuta.getActionItems().get(0).getDescripcion(),"cambie");

    }

    @Test
    public void test22UnNuevoUsuarioSePersisteConSuEmail(){
        String mail = "pepe@10pines.com";
        Usuario usuario = Usuario.create("pepe", "pepe", "pepe", "9000", mail);
        usuarioService.save(usuario);
        Usuario savedUser = usuarioService.getAll().stream().filter(user -> user.getLogin().equals("pepe")).findFirst().get();
        assertThat(savedUser.getMail()).isEqualTo(mail);
    }

    private void startApplication(){
        application = TestApplication.create(TestConfig.create());
        application.start();
    }
}
