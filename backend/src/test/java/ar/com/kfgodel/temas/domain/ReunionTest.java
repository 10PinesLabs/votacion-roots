package ar.com.kfgodel.temas.domain;

import ar.com.kfgodel.temas.helpers.TestHelper;
import ar.com.kfgodel.temas.model.OrdenarPorVotos;
import convention.persistent.Reunion;
import convention.persistent.StatusDeReunion;
import convention.persistent.TemaDeReunion;
import convention.persistent.Usuario;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by sandro on 19/06/17.
 */
public class ReunionTest {

    private TestHelper helper;

    @Before
    public void setUp(){
        helper = new TestHelper();
    }

    @Test
    public void test01AlCrearUnaReunionSuEstadoEsPendiente(){
        Reunion unaReunion = Reunion.create(LocalDate.of(2017, 06, 16));
        Assert.assertEquals(StatusDeReunion.PENDIENTE, unaReunion.getStatus());
    }

    @Test
    public void test02AlCrearUnaReunionNoTieneTemas(){
        Reunion unaReunion = Reunion.create(LocalDate.of(2017, 06, 16));
        Assert.assertEquals(0, unaReunion.getTemasPropuestos().size());
    }

    @Test
    public void test03AlCerrarUnaReunionSuEstadoEsCerrada(){
        Reunion unaReunion = Reunion.create(LocalDate.of(2017, 06, 16));
        unaReunion.cerrarVotacion();
        Assert.assertEquals(StatusDeReunion.CERRADA, unaReunion.getStatus());
    }

    @Test
    public void test04AlCerrarUnaReunionLosTemasPropuestosSeOrdenanPorCantidadDeVotos() {
        Reunion unaReunion = Reunion.create(LocalDate.of(2017, 06, 16));
        Usuario unUsuario = new Usuario();
        TemaDeReunion tema1 = helper.nuevoTemaNoObligatorio();
        TemaDeReunion tema2 = helper.nuevoTemaNoObligatorio();
        TemaDeReunion tema3 = helper.nuevoTemaNoObligatorio();
        List<TemaDeReunion> temasDeLaReunion = Arrays.asList(tema1, tema2, tema3);
        unaReunion.setTemasPropuestos(temasDeLaReunion);

        //Agrego 2 votos a la propuesta 1
        tema1.agregarInteresado(unUsuario);
        tema1.agregarInteresado(unUsuario);

        //Agrego 1 votos a la propuesta 2
        tema2.agregarInteresado(unUsuario);

        //Agrego 3 votos a la propuesta 3
        tema3.agregarInteresado(unUsuario);
        tema3.agregarInteresado(unUsuario);
        tema3.agregarInteresado(unUsuario);

        unaReunion.cerrarVotacion();

        Assert.assertEquals(tema3, unaReunion.getTemasPropuestos().get(0));
        Assert.assertEquals(tema1, unaReunion.getTemasPropuestos().get(1));
        Assert.assertEquals(tema2, unaReunion.getTemasPropuestos().get(2));
    }

    @Test
    public void test05AlReabrirUnaReunionCerradaSuEstadoEsPendiente(){
        Reunion unaReunion = Reunion.create(LocalDate.of(2017, 06, 16));
        unaReunion.cerrarVotacion();
        unaReunion.reabrirVotacion();
        Assert.assertEquals(StatusDeReunion.PENDIENTE, unaReunion.getStatus());
    }

    // No estoy seguro de que vaya acá
    @Test
    public void test06ElOrdenadorDeTemasOrdenaCorrectamenteUnConjuntoDeTemas() {
        Usuario unUsuario = new Usuario();

        TemaDeReunion tema1 = helper.nuevoTemaObligatorio();
        tema1.setMomentoDeCreacion(LocalDateTime.of(2017, 06, 26, 0, 0));

        TemaDeReunion tema2 = helper.nuevoTemaObligatorio();
        tema2.setMomentoDeCreacion(LocalDateTime.of(2018, 06, 26, 0, 0));

        TemaDeReunion tema3 = helper.nuevoTemaNoObligatorio();
        tema3.setMomentoDeCreacion(LocalDateTime.of(2018, 02, 26, 0, 0));
        tema3.agregarInteresado(unUsuario);
        tema3.agregarInteresado(unUsuario);

        TemaDeReunion tema4 = helper.nuevoTemaNoObligatorio();
        tema4.setMomentoDeCreacion(LocalDateTime.of(2016, 02, 26, 0, 0));
        tema4.agregarInteresado(unUsuario);

        TemaDeReunion tema5 = helper.nuevoTemaNoObligatorio();
        tema5.setMomentoDeCreacion(LocalDateTime.of(2017, 05, 26, 0, 0));
        tema5.agregarInteresado(unUsuario);

        List<TemaDeReunion> temas = Arrays.asList(tema1, tema2, tema3, tema4, tema5);
        temas.sort(Collections.reverseOrder(OrdenarPorVotos.create()));

        Assert.assertEquals(tema1, temas.get(0));
        Assert.assertEquals(tema2, temas.get(1));
        Assert.assertEquals(tema3, temas.get(2));
        Assert.assertEquals(tema4, temas.get(3));
        Assert.assertEquals(tema5, temas.get(4));
    }

    @Test
    public void test07NoSeReorganizanLosTemasPorCadaVezQueSeVota() {
        Reunion unaReunion = Reunion.create(LocalDate.of(2017, 06, 16));
        Usuario unUsuario = new Usuario();
        TemaDeReunion tema1 = helper.nuevoTemaNoObligatorio();
        TemaDeReunion tema2 = helper.nuevoTemaNoObligatorio();
        TemaDeReunion tema3 = helper.nuevoTemaNoObligatorio();
        List<TemaDeReunion> temasDeLaReunion = Arrays.asList(tema1, tema2, tema3);
        unaReunion.setTemasPropuestos(temasDeLaReunion);

        tema3.agregarInteresado(unUsuario);

        Assert.assertEquals(tema1, unaReunion.getTemasPropuestos().get(0));
        Assert.assertEquals(tema2, unaReunion.getTemasPropuestos().get(1));
        Assert.assertEquals(tema3, unaReunion.getTemasPropuestos().get(2));
    }
}
