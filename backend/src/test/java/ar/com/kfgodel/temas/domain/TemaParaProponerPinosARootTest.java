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
    public void sePuedeAgregarUnaPropuestaDePinoARoot() {
        TemaParaProponerPinosARoot unTemaParaProponerPinos = new TemaParaProponerPinosARoot();

        PropuestaDePinoARoot unaPropuesta = helper.unaPropuestaDeUnPinoARoot();
        unTemaParaProponerPinos.agregarPropuesta(unaPropuesta);

        assertThat(unTemaParaProponerPinos.propuestas()).containsExactly(unaPropuesta);
    }

    @Test
    public void sePuedeAgregarMasDeUnaPropuestaDePinoARoot() {
        TemaParaProponerPinosARoot unTemaParaProponerPinos = new TemaParaProponerPinosARoot();

        PropuestaDePinoARoot unaPropuesta = helper.unaPropuestaDeUnPinoARoot();
        PropuestaDePinoARoot otraPropuesta = helper.unaPropuestaDeOtroPinoARoot();
        unTemaParaProponerPinos.agregarPropuesta(unaPropuesta);
        unTemaParaProponerPinos.agregarPropuesta(otraPropuesta);

        Collection<PropuestaDePinoARoot> propuestas = Arrays.asList(unaPropuesta, otraPropuesta);
        assertThat(unTemaParaProponerPinos.propuestas()).containsExactlyElementsOf(propuestas);
    }

    @Test
    public void noSePuedeAgregarLaMismaPropuestaDePinoARootMasDeUnaVez() {
        TemaParaProponerPinosARoot unTemaParaProponerPinos = new TemaParaProponerPinosARoot();
        PropuestaDePinoARoot unaPropuesta = helper.unaPropuestaDeUnPinoARoot();
        unTemaParaProponerPinos.agregarPropuesta(unaPropuesta);

        assertThatThrownBy(() -> {
            unTemaParaProponerPinos.agregarPropuesta(unaPropuesta);
        }).hasMessage(TemaParaProponerPinosARoot.PINO_YA_PROPUESTO_ERROR_MSG);
    }

    @Test
    public void noSePuedeProponerAlMismoPinoMasDeUnaVez() {
        PropuestaDePinoARoot unaPropuesta = helper.unaPropuestaDeUnPinoARootSponsoreadoPor(helper.unUsuario(), this);
        PropuestaDePinoARoot otraPropuesta = helper.unaPropuestaDeUnPinoARootSponsoreadoPor(helper.otroUsuario(), this);
        TemaParaProponerPinosARoot unTemaParaProponerPinos = new TemaParaProponerPinosARoot();
        unTemaParaProponerPinos.agregarPropuesta(unaPropuesta);

        assertThatThrownBy(() -> {
            unTemaParaProponerPinos.agregarPropuesta(otraPropuesta);
        }).hasMessage(TemaParaProponerPinosARoot.PINO_YA_PROPUESTO_ERROR_MSG);
    }

    @Test
    public void losTemasParaProponerPinosARootTienenUnTituloFijo() {
        TemaParaProponerPinosARoot unTemaParaProponerPinos = new TemaParaProponerPinosARoot();

        assertThat(unTemaParaProponerPinos.getTitulo()).isEqualTo(TemaParaProponerPinosARoot.TITULO);
    }

    @Test
    public void losTemasParaProponerPinosARootNoSonObligatorios() {
        TemaParaProponerPinosARoot unTemaParaProponerPinos = new TemaParaProponerPinosARoot();

        assertThat(unTemaParaProponerPinos.getObligatoriedad()).isEqualTo(ObligatoriedadDeTema.OBLIGATORIO);
    }

    @Test
    public void losTemasParaProponerPinosARootTienenDuracionCorta() {
        TemaParaProponerPinosARoot unTemaParaProponerPinos = new TemaParaProponerPinosARoot();

        assertThat(unTemaParaProponerPinos.getDuracion()).isEqualTo(DuracionDeTema.CORTO);
    }

}
