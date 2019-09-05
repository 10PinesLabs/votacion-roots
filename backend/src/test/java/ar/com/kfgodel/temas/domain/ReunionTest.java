package ar.com.kfgodel.temas.domain;

import ar.com.kfgodel.temas.helpers.TestHelper;
import ar.com.kfgodel.temas.model.OrdenarPorVotos;
import convention.persistent.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Created by sandro on 19/06/17.
 */
public class ReunionTest {

    private TestHelper helper;

    @Before
    public void setUp() {
        helper = new TestHelper();
    }

    @Test
    public void testAlCrearUnaReunionSuEstadoEsPendiente() {
        Reunion unaReunion = helper.unaReunion();
        Assert.assertEquals(StatusDeReunion.PENDIENTE, unaReunion.getStatus());
    }

    @Test
    public void testAlCrearUnaReunionNoTieneTemas() {
        Reunion unaReunion = helper.unaReunion();
        Assert.assertEquals(0, unaReunion.getTemasPropuestos().size());
    }

    @Test
    public void testAlCerrarUnaReunionSuEstadoEsCerrada() {
        Reunion unaReunion = helper.unaReunion();
        unaReunion.cerrarVotacion();
        Assert.assertEquals(StatusDeReunion.CERRADA, unaReunion.getStatus());
    }

    @Test
    public void testAlCerrarUnaReunionLosTemasPropuestosSeOrdenanPorCantidadDeVotos() {
        Reunion unaReunion = helper.unaReunion();
        Usuario unUsuario = helper.unUsuario();
        TemaDeReunion tema1 = helper.unTemaNoObligatorio();
        TemaDeReunion tema2 = helper.unTemaNoObligatorio();
        TemaDeReunion tema3 = helper.unTemaNoObligatorio();
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
    public void testAlReabrirUnaReunionCerradaSuEstadoEsPendiente() {
        Reunion unaReunion = helper.unaReunion();
        unaReunion.cerrarVotacion();
        unaReunion.reabrirVotacion();
        Assert.assertEquals(StatusDeReunion.PENDIENTE, unaReunion.getStatus());
    }

    // No estoy seguro de que vaya acá
    @Test
    public void testElOrdenadorDeTemasOrdenaCorrectamenteUnConjuntoDeTemas() {
        Usuario unUsuario = helper.unUsuario();

        TemaDeReunion tema1 = helper.unTemaObligatorio();
        tema1.setMomentoDeCreacion(LocalDateTime.of(2017, 06, 26, 0, 0));

        TemaDeReunion tema2 = helper.unTemaObligatorio();
        tema2.setMomentoDeCreacion(LocalDateTime.of(2018, 06, 26, 0, 0));

        TemaDeReunion tema3 = helper.unTemaNoObligatorio();
        tema3.setMomentoDeCreacion(LocalDateTime.of(2018, 02, 26, 0, 0));
        tema3.agregarInteresado(unUsuario);
        tema3.agregarInteresado(unUsuario);

        TemaDeReunion tema4 = helper.unTemaNoObligatorio();
        tema4.setMomentoDeCreacion(LocalDateTime.of(2016, 02, 26, 0, 0));
        tema4.agregarInteresado(unUsuario);

        TemaDeReunion tema5 = helper.unTemaNoObligatorio();
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
    public void testNoSeReorganizanLosTemasPorCadaVezQueSeVota() {
        Reunion unaReunion = helper.unaReunion();
        Usuario unUsuario = helper.unUsuario();
        TemaDeReunion tema1 = helper.unTemaNoObligatorio();
        TemaDeReunion tema2 = helper.unTemaNoObligatorio();
        TemaDeReunion tema3 = helper.unTemaNoObligatorio();
        List<TemaDeReunion> temasDeLaReunion = Arrays.asList(tema1, tema2, tema3);
        unaReunion.setTemasPropuestos(temasDeLaReunion);

        tema3.agregarInteresado(unUsuario);

        Assert.assertEquals(tema1, unaReunion.getTemasPropuestos().get(0));
        Assert.assertEquals(tema2, unaReunion.getTemasPropuestos().get(1));
        Assert.assertEquals(tema3, unaReunion.getTemasPropuestos().get(2));
    }

    @Test
    public void testAlObtenerLosUsuariosQueVotaronEstosNoAparecenDuplicados() {
        Reunion unaReunion = helper.unaReunion();
        Usuario unUsuario = helper.unUsuario();
        TemaDeReunion tema1 = helper.unTemaNoObligatorio();
        TemaDeReunion tema2 = helper.unTemaNoObligatorio();
        tema1.setInteresados(Arrays.asList(unUsuario));
        tema2.setInteresados(Arrays.asList(unUsuario, unUsuario));

        unaReunion.setTemasPropuestos(Arrays.asList(tema1, tema2));

        assertThat(unaReunion.usuariosQueVotaron()).containsExactly(unUsuario);
    }

    @Test
    public void testProponerAUnPinoComoRootCreaUnTemaParaEso() {
        Reunion unaReunion = helper.unaReunion();

        String unPino = helper.unPino();
        Usuario unSponsor = helper.unUsuario();
        unaReunion.proponerPinoComoRoot(unPino, unSponsor);

        TemaDeReunion temaCreado = unaReunion.getTemasPropuestos().get(0);
        assertThat(temaCreado.esParaProponerPinosARoot()).isTrue();
    }

    @Test
    public void testElTemaCreadoAlProponerAUnPinoComoRootTieneAlPinoPropuestoYASuSponsor() {
        Reunion unaReunion = helper.unaReunion();

        String unPino = helper.unPino();
        Usuario unSponsor = helper.unUsuario();
        unaReunion.proponerPinoComoRoot(unPino, unSponsor);

        TemaParaProponerPinosARoot temaCreado = (TemaParaProponerPinosARoot) unaReunion.getTemasPropuestos().get(0);
        Collection<PropuestaDePinoARoot> propuestas = temaCreado.propuestas();
        assertThatExistePropuestaPara(propuestas, unPino, unSponsor);
    }

    @Test
    public void testProponerAOtroPinoComoRootNoCreaOtroTema() {
        Reunion unaReunion = helper.unaReunion();
        String unPino = helper.unPino();
        Usuario unSponsor = helper.unUsuario();
        unaReunion.proponerPinoComoRoot(unPino, unSponsor);

        String otroPino = helper.otroPino();
        unaReunion.proponerPinoComoRoot(otroPino, unSponsor);

        assertThat(unaReunion.getTemasPropuestos()).hasSize(1);
    }

    @Test
    public void testElTemaParaProponerPinosComoRootsTieneATodosLosPinosPropuestos() {
        Reunion unaReunion = helper.unaReunion();

        String unPino = helper.unPino();
        String otroPino = helper.otroPino();
        Usuario unSponsor = helper.unUsuario();
        unaReunion.proponerPinoComoRoot(unPino, unSponsor);
        unaReunion.proponerPinoComoRoot(otroPino, unSponsor);

        TemaParaProponerPinosARoot temaCreado = (TemaParaProponerPinosARoot) unaReunion.getTemasPropuestos().get(0);
        Collection<PropuestaDePinoARoot> propuestas = temaCreado.propuestas();
        assertThatExistePropuestaPara(propuestas, unPino, unSponsor);
        assertThatExistePropuestaPara(propuestas, otroPino, unSponsor);
    }

    @Test
    public void testProponerAUnPinoComoRootCreaUnTemaParaEsoCuandoYaHabianOtrosTemasComunes() {
        Reunion unaReunion = helper.unaReunion();
        unaReunion.agregarTema(helper.unTemaDeReunion());

        String unPino = helper.unPino();
        Usuario unSponsor = helper.unUsuario();
        unaReunion.proponerPinoComoRoot(unPino, unSponsor);

        assertThat(unaReunion.getTemasPropuestos()).hasSize(2);
    }

    @Test
    public void testNoSePuedeAgregarUnTemaConTituloDeTemaParaProponerPinosComoRoots() {
        Reunion unaReunion = helper.unaReunion();
        String unPino = helper.unPino();
        Usuario unSponsor = helper.unUsuario();
        unaReunion.proponerPinoComoRoot(unPino, unSponsor);

        TemaDeReunion unTemaConTituloDeTemaParaProponerPinos = helper.unTemaDeReunionConTitulo(TemaParaProponerPinosARoot.TITULO);

        assertThatThrownBy(() -> {
            unaReunion.agregarTema(unTemaConTituloDeTemaParaProponerPinos);
        }).hasMessage(Reunion.AGREGAR_TEMA_PARA_PROPONER_PINOS_COMO_ROOT_ERROR_MSG);
    }

    @Test
    public void testElAutorDelTemaParaProponerPinosComoRootsEsElPrimerUsuarioQueHizoUnaPropuesta() {
        Reunion unaReunion = helper.unaReunion();

        String unPino = helper.unPino();
        Usuario unSponsor = helper.unUsuario();
        unaReunion.proponerPinoComoRoot(unPino, unSponsor);

        TemaDeReunion temaCreado = unaReunion.getTemasPropuestos().get(0);
        assertThat(temaCreado.getAutor()).isEqualTo(unSponsor);
    }

    private void assertThatExistePropuestaPara(Collection<PropuestaDePinoARoot> propuestas, String unPino, Usuario unSponsor) {
        assertThat(propuestas).anyMatch(
                propuesta -> propuesta.pino().equals(unPino) && propuesta.sponsor() == unSponsor);
    }
}
