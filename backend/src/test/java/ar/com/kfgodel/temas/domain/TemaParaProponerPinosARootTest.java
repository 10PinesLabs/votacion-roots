package ar.com.kfgodel.temas.domain;

import ar.com.kfgodel.temas.helpers.TestHelper;
import convention.persistent.*;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TemaParaProponerPinosARootTest {

    private TestHelper helper = new TestHelper();

    @Test
    public void sePuedeCrearUnTemaParaProponerPinosARootConUnaPropuesta() {
        PropuestaDePinoARoot unaPropuesta = helper.unaPropuestaDeUnPinoARoot();

        TemaParaProponerPinosARoot unTemaParaProponerPinos = new TemaParaProponerPinosARoot(unaPropuesta);

        assertThat(unTemaParaProponerPinos.propuestas()).containsExactly(unaPropuesta);
    }

    @Test
    public void sePuedeAgregarUnaPropuestaDePinoARoot() {
        PropuestaDePinoARoot unaPropuesta = helper.unaPropuestaDeUnPinoARoot();
        PropuestaDePinoARoot otraPropuesta = helper.unaPropuestaDeOtroPinoARoot();
        Collection<PropuestaDePinoARoot> propuestas = Arrays.asList(unaPropuesta, otraPropuesta);
        TemaParaProponerPinosARoot unTemaParaProponerPinos = new TemaParaProponerPinosARoot(unaPropuesta);

        unTemaParaProponerPinos.agregarPropuesta(otraPropuesta);

        assertThat(unTemaParaProponerPinos.propuestas()).containsExactlyElementsOf(propuestas);
    }

    @Test
    public void noSePuedeAgregarLaMismaPropuestaDePinoARootMasDeUnaVez() {
        PropuestaDePinoARoot unaPropuesta = helper.unaPropuestaDeUnPinoARoot();
        TemaParaProponerPinosARoot unTemaParaProponerPinos = new TemaParaProponerPinosARoot(unaPropuesta);

        assertThatThrownBy(() -> {
            unTemaParaProponerPinos.agregarPropuesta(unaPropuesta);
        }).hasMessage(TemaParaProponerPinosARoot.PINO_YA_PROPUESTO_ERROR_MSG);
    }

    @Test
    public void noSePuedeProponerAlMismoPinoMasDeUnaVez() {
        PropuestaDePinoARoot unaPropuesta = helper.unaPropuestaDeUnPinoARootSponsoreadoPor(helper.unSponsor(), this);
        PropuestaDePinoARoot otraPropuesta = helper.unaPropuestaDeUnPinoARootSponsoreadoPor(helper.otroSponsor(), this);
        TemaParaProponerPinosARoot unTemaParaProponerPinos = new TemaParaProponerPinosARoot(unaPropuesta);

        assertThatThrownBy(() -> {
            unTemaParaProponerPinos.agregarPropuesta(otraPropuesta);
        }).hasMessage(TemaParaProponerPinosARoot.PINO_YA_PROPUESTO_ERROR_MSG);
    }

    @Test
    public void sePuedeAgregarUnTemaParaProponerPinosARootAUnaReunion() {
        Reunion reunion = Reunion.create(LocalDate.of(2019, 1, 1));
        TemaParaProponerPinosARoot unTemaParaProponerPinos = helper.unTemaParaProponerPinosARoot();

        reunion.agregarTema(unTemaParaProponerPinos);

        assertThat(reunion.getTemasPropuestos()).containsExactly(unTemaParaProponerPinos);
    }

    @Test
    public void losTemasParaProponerPinosARootTienenUnTituloFijo() {
        TemaParaProponerPinosARoot unTemaParaProponerPinos = helper.unTemaParaProponerPinosARoot();

        assertThat(unTemaParaProponerPinos.getTitulo()).isEqualTo(TemaParaProponerPinosARoot.TITULO);
    }

    @Test
    public void losTemasParaProponerPinosARootNoSonObligatorios() {
        TemaParaProponerPinosARoot unTemaParaProponerPinos = helper.unTemaParaProponerPinosARoot();

        assertThat(unTemaParaProponerPinos.getObligatoriedad()).isEqualTo(ObligatoriedadDeTema.OBLIGATORIO);
    }

    @Test
    public void losTemasParaProponerPinosARootTienenDuracionCorta() {
        TemaParaProponerPinosARoot unTemaParaProponerPinos = helper.unTemaParaProponerPinosARoot();

        assertThat(unTemaParaProponerPinos.getDuracion()).isEqualTo(DuracionDeTema.CORTO);
    }

}
