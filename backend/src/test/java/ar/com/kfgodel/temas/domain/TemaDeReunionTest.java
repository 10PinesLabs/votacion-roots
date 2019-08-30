package ar.com.kfgodel.temas.domain;

import ar.com.kfgodel.temas.helpers.TestHelper;
import convention.persistent.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static junit.framework.TestCase.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by sandro on 19/06/17.
 */
public class TemaDeReunionTest {

    private TestHelper helper;

    @Before
    public void setUp(){
        helper = new TestHelper();
    }

    @Test
    public void test01AlAgregarUnInteresadoAlTemaLaCantidadDeVotosAumentaEn1() throws Exception {
        TemaDeReunion unTema = TemaDeReunionConDescripcion.create();
        Usuario unUsuario = new Usuario();
        unTema.agregarInteresado(unUsuario);
        Assert.assertEquals(1, unTema.getCantidadDeVotos());
    }

    @Test
    public void test02AlBorrarUnInteresadoDelTemaLaCantidadDeVotosDisminuyeEn1() throws Exception {
        TemaDeReunion unTema = TemaDeReunionConDescripcion.create();
        Usuario unUsuario = new Usuario();
        unTema.agregarInteresado(unUsuario);
        unTema.quitarInteresado(unUsuario);
        Assert.assertEquals(0, unTema.getCantidadDeVotos());
    }

    @Test
    public void test03UnTemaObligatorioNoPuedeSerVotado(){
        TemaDeReunion unTemaObligatorio = helper.unTemaObligatorio();
        Assert.assertFalse(unTemaObligatorio.puedeSerVotado());
    }

    @Test
    public void test04UnTemaNoObligatorioPuedeSerVotado(){
        TemaDeReunion unTemaNoObligatorio = helper.unTemaNoObligatorio();
        Assert.assertTrue(unTemaNoObligatorio.puedeSerVotado());
    }

    @Test
    public void test05NoSePuedenAgregarInteresadosAUnTemaObligatorio(){
        TemaDeReunion unTemaObligatorio = helper.unTemaObligatorio();
        Usuario unUsuario = new Usuario();
        try{
            unTemaObligatorio.agregarInteresado(unUsuario);
            fail("No debería permitir agregar un interesado a un tema obligatorio");
        }catch (Exception exception){
            Assert.assertThat(exception.getMessage(), is(TemaDeReunion.ERROR_AGREGAR_INTERESADO));
        }
    }

    @Test
    public void test06UnTemaObligatorioTieneMayorPrioridadQueUnoNoObligatorio(){
        TemaDeReunion unTemaObligatorio = helper.unTemaObligatorio();
        TemaDeReunion unTemaNoObligatorio = helper.unTemaNoObligatorio();

        Assert.assertTrue(unTemaObligatorio.tieneMayorPrioridadQue(unTemaNoObligatorio));
    }

    @Test
    public void test07UnTemaNoObligatorioNoTieneMayorPrioridadQueUnoObligatorio(){
        TemaDeReunion unTemaObligatorio = helper.unTemaObligatorio();
        TemaDeReunion unTemaNoObligatorio = helper.unTemaNoObligatorio();

        Assert.assertFalse(unTemaNoObligatorio.tieneMayorPrioridadQue(unTemaObligatorio));
    }

    @Test
    public void test08UnTemaObligatorioTieneMayorPrioridadQueOtroSiFueCreadoAntes(){
        TemaDeReunion primerTemaObligatorio = helper.unTemaObligatorio();
        primerTemaObligatorio.setMomentoDeCreacion(LocalDateTime.of(2017, 06, 26, 0, 0));

        TemaDeReunion segundoTemaObligatorio = helper.unTemaObligatorio();
        segundoTemaObligatorio.setMomentoDeCreacion(LocalDateTime.of(2018, 06, 26, 0, 0));

        Assert.assertTrue(primerTemaObligatorio.tieneMayorPrioridadQue(segundoTemaObligatorio));
    }

    @Test
    public void test09UnTemaNoObligatorioTieneMayorPrioridadQueOtroSiTieneMasVotos() throws Exception {
        Usuario unUsuario = new Usuario();

        TemaDeReunion temaMasVotado = helper.unTemaNoObligatorio();
        temaMasVotado.setMomentoDeCreacion(LocalDateTime.of(2017, 06, 26, 0, 0));
        temaMasVotado.agregarInteresado(unUsuario);

        TemaDeReunion temaMenosVotado = helper.unTemaNoObligatorio();
        temaMenosVotado.setMomentoDeCreacion(LocalDateTime.of(2016, 06, 26, 0, 0));

        Assert.assertTrue(temaMasVotado.tieneMayorPrioridadQue(temaMenosVotado));
    }

    @Test
    public void test10UnTemaNoObligatorioTieneMayorPrioridadQueOtroSiTienenLaMismaCantidadDeVotosYFueCreadoAntes() throws Exception {
        Usuario unUsuario = new Usuario();

        TemaDeReunion primerTema = helper.unTemaNoObligatorio();
        primerTema.setMomentoDeCreacion(LocalDateTime.of(2017, 06, 26, 0, 0));
        primerTema.agregarInteresado(unUsuario);

        TemaDeReunion segundoTema = helper.unTemaNoObligatorio();
        segundoTema.setMomentoDeCreacion(LocalDateTime.of(2018, 06, 26, 0, 0));
        segundoTema.agregarInteresado(unUsuario);

        Assert.assertTrue(primerTema.tieneMayorPrioridadQue(segundoTema));
    }

    @Test
    public void test11UnTemaDeReunionSabeCuandoNoFueGeneradoPorUnTemaGeneral(){
        TemaDeReunion temaDeReunion = TemaDeReunionConDescripcion.create();
        Assert.assertFalse(temaDeReunion.fueGeneradoPorUnTemaGeneral());
    }

    @Test
    public void test12UnTemaDeReunionSabeCuandoSiFueGeneradoPorUnTemaGeneral(){
        Reunion unaReunion = Reunion.create(LocalDate.of(2017, 06, 26));
        TemaGeneral temaGeneral = new TemaGeneral();
        TemaDeReunion temaDeReunion = temaGeneral.generarTemaPara(unaReunion);

        Assert.assertTrue(temaDeReunion.fueGeneradoPorUnTemaGeneral());
    }

    @Test
    public void test13UnTemaGeneradoPorUnTemaGeneralTieneMajorPrioridadQueUnoObligatorio(){
        TemaDeReunion temaObligatorio = helper.unTemaObligatorio();
        TemaDeReunion temaObligatorioGeneral = helper.unTemaAPartirDeUnTemaGeneral();

        Assert.assertTrue(temaObligatorioGeneral.tieneMayorPrioridadQue(temaObligatorio));
    }

    @Test
    public void test14SiDosTemasFueronGeneradosPorTemasGeneralesTieneMayorPrioridadElQueFueCreadoAntes(){
        TemaDeReunion temaAnterior = helper.unTemaAPartirDeUnTemaGeneral();
        temaAnterior.setMomentoDeCreacion(LocalDateTime.of(2017,06,20,0,0));
        TemaDeReunion temaPosterior = helper.unTemaAPartirDeUnTemaGeneral();
        temaPosterior.setMomentoDeCreacion(LocalDateTime.of(2018,06,20,0,0));

        Assert.assertTrue(temaAnterior.tieneMayorPrioridadQue(temaPosterior));
    }

    @Test
    public void test15SiNoModificonadaDeUnTemaEspecificoQuedaComoQueNoEstaModificado(){
        TemaDeReunion tema = helper.unTemaAPartirDeUnTemaGeneral();

        Assert.assertFalse(tema.fueModificado());
    }

    @Test
    public void test16SiModificoLaDescripcionUnTemaEspecificoSeMarcaComoModificado(){
        TemaDeReunion tema = helper.unTemaAPartirDeUnTemaGeneral();
        tema.setDescripcion("otra descipcion");

        Assert.assertTrue(tema.fueModificado());
    }

    @Test
    public void test17SiModificoLaDuracionUnTemaEspecificoSeMarcaComoModificado(){
        TemaDeReunion tema = helper.unTemaAPartirDeUnTemaGeneral();
        tema.setDuracion(DuracionDeTema.CORTO);

        Assert.assertTrue(tema.fueModificado());
    }

    @Test
    public void test18SiModificoLaObligatoriedadUnTemaEspecificoSeMarcaComoModificado(){
        TemaDeReunion tema = helper.unTemaAPartirDeUnTemaGeneral();
        tema.setObligatoriedad(ObligatoriedadDeTema.NO_OBLIGATORIO);

        Assert.assertTrue(tema.fueModificado());
    }

    @Test
    public void test19SiModificoElTituloUnTemaEspecificoSeMarcaComoModificado(){
        TemaDeReunion tema = helper.unTemaAPartirDeUnTemaGeneral();
        tema.setTitulo("otro titulo");

        Assert.assertTrue(tema.fueModificado());
    }

    @Test
    public void test20LosTemasTienenUnaPropuestaOriginal() {
        TemaDeReunion unaPropuestaOriginal = helper.unTemaDeReunion();
        TemaDeReunion unTema = helper.unaRePropuestaDe(unaPropuestaOriginal);

        assertThat(unTema.getPropuestaOriginal()).isEqualTo(unaPropuestaOriginal);
    }

    @Test
    public void testUnTemaEsUnaRePropuestaSiSuPropuestaOriginalEsOtroTemaDistinto() {
        TemaDeReunion unaPropuestaOriginal = helper.unTemaDeReunion();
        TemaDeReunion unTema = helper.unaRePropuestaDe(unaPropuestaOriginal);

        assertThat(unTema.esRePropuesta()).isTrue();
    }

    @Test
    public void testUnTemaNoEsRePropuestaSiSuPropuestaOriginalEsElMismo() {
        TemaDeReunion unTema = helper.unTemaDeReunion();
        unTema.setPropuestaOriginal(unTema);

        assertThat(unTema.esRePropuesta()).isFalse();
    }

    @Test
    public void testDosTemasReProponenElMismoTemaSiSuPropuestaOriginalSonLaMisma() {
        TemaDeReunion unaPropuestaOriginal = helper.unTemaDeReunion();
        TemaDeReunion unTema = helper.unaRePropuestaDe(unaPropuestaOriginal);
        TemaDeReunion otroTema = helper.unaRePropuestaDe(unaPropuestaOriginal);

        assertThat(unTema.reProponeElMismoTemaQue(otroTema)).isTrue();
    }

    @Test
    public void testDosTemasNoReProponenElMismoTemaSiSuPropuestaOriginalEsDistinta() {
        TemaDeReunion unaPropuestaOriginal = helper.unTemaDeReunion();
        TemaDeReunion otraPropuestaOriginal = helper.unTemaDeReunion();
        TemaDeReunion unTema = helper.unaRePropuestaDe(unaPropuestaOriginal);
        TemaDeReunion otroTema = helper.unaRePropuestaDe(otraPropuestaOriginal);

        assertThat(unTema.reProponeElMismoTemaQue(otroTema)).isFalse();
    }

    @Test
    public void testLaFechaDeLaPrimeraPropuestaEsLaFechaDeLaReunionDeLaPrimeraPropuesta() {
        Reunion unaReunion = helper.unaReunion();
        TemaDeReunion unaPrimeraPropuesta = helper.unTemaDeReunion(unaReunion);
        TemaDeReunion unTema = helper.unaRePropuestaDe(unaPrimeraPropuesta);

        assertThat(unTema.getFechaDePrimeraPropuesta()).isEqualTo(unaReunion.getFecha());
    }
}
