package ar.com.kfgodel.temas.domain;

import ar.com.kfgodel.temas.helpers.TestHelper;
import convention.persistent.*;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


/**
 * Created by sandro on 06/07/17.
 */
public class MinutaTest {

    private TestHelper helper = new TestHelper();

    @Test
    public void test01UnaMinutaNuevaNoTieneAsistentesSiNingunTemaFueVotado() {
        Reunion reunion = crearReunion();
        Minuta minuta = Minuta.create(reunion);
        Assert.assertEquals(0, minuta.getAsistentes().size());
        assertThat(minuta.getAsistentes()).hasSize(0);
    }

    @Test
    public void test02UnaMinutaNuevaTieneComoAsistentesALosVotantesDeLosTemas() {
        Reunion reunion = crearReunion();
        TemaDeReunion unTema = crearTema();
        Usuario unVotante = crearUsuario();
        unTema.setInteresados(Arrays.asList(unVotante));
        reunion.setTemasPropuestos(Arrays.asList(unTema));
        Minuta minuta = Minuta.create(reunion);

        assertThat(minuta.getAsistentes()).contains(unVotante);
    }

    @Test
    public void test03UnaMinutaTieneUnSoloAsistentePorMasQueHayaVotadoMasDeUnaVezLaMismaPersona() {
        Reunion reunion = crearReunion();
        TemaDeReunion unTema = crearTema();
        Usuario unVotante = crearUsuario();
        unTema.setInteresados(Arrays.asList(unVotante, unVotante, unVotante));
        reunion.setTemasPropuestos(Arrays.asList(unTema));
        Minuta minuta = Minuta.create(reunion);

        assertThat(minuta.getAsistentes()).hasSize(1);
    }

    @Test
    public void test04UnaMinutaTieneLosTemasPropuestosConLaMismaCantidadDeVotantes() {
        Reunion reunion = crearReunion();

        TemaDeReunion unTema = crearTema();
        TemaDeReunion otroTema = crearTema();

        Usuario unVotante = crearUsuario();

        unTema.setInteresados(Arrays.asList(unVotante));
        otroTema.setInteresados(Arrays.asList(unVotante, unVotante));

        reunion.setTemasPropuestos(Arrays.asList(unTema, otroTema));
        Minuta minuta = Minuta.create(reunion);

        assertThat(minuta.getAsistentes()).containsExactly(unVotante);
        List<Integer> cantidadDeVotantesPorTema = minuta.getReunion().getTemasPropuestos().stream().map(tema -> tema.getInteresados().size()).collect(Collectors.toList());
        assertThat(cantidadDeVotantesPorTema)
                .isEqualTo(Arrays.asList(1, 2));
    }

    @Test
    public void testNoSePuedenCargarActionItemsCuandoNoHayUnTemaParaEso() {
        Minuta unaMinuta = helper.unaMinuta();
        Reunion unaReunion = helper.unaReunion();
        unaReunion.cargarSiExisteElTemaParaRepasarActionItemsDe(unaMinuta);
        assertThat(unaReunion.getTemasPropuestos().size()).isEqualTo(0);
    }

    @Test
    public void testSePuedenCargarActionItemsCuandoHayUnTemaParaEso() {
        Minuta unaMinuta = helper.unaMinuta();
        Reunion unaReunion = helper.unaReunion();
        TemaDeReunion unTemaConTituloRepasarActionItems = helper.unTemaDeReunionConTitulo(TemaParaRepasarActionItems.TITULO);
        unaReunion.agregarTema(unTemaConTituloRepasarActionItems);

        unaReunion.cargarSiExisteElTemaParaRepasarActionItemsDe(unaMinuta);

        List<TemaDeReunion> temasPropuestos = unaReunion.getTemasPropuestos();
        assertThat(temasPropuestos).hasSize(1);
        assertThat(temasPropuestos).anyMatch(TemaDeReunion::esParaRepasarActionItems);
    }

    private TemaDeReunion crearTema() {
        return TemaDeReunionConDescripcion.create();
    }

    private Reunion crearReunion() {
        return Reunion.create(LocalDate.of(2017, 6, 26));
    }

    private Usuario crearUsuario() {
        return Usuario.create("Juan", "Juan", "juan", "88", "juan@10pines.com");
    }
}
