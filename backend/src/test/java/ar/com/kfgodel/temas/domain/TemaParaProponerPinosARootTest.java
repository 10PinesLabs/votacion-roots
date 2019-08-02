package ar.com.kfgodel.temas.domain;

import convention.persistent.PropuestaDePinoARoot;
import convention.persistent.TemaParaProponerPinosARoot;
import convention.persistent.Usuario;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.*;

public class TemaParaProponerPinosARootTest {

    @Test
    public void sePuedeCrearUnTemaParaProponerPinosARootConUnaPropuesta() {
        PropuestaDePinoARoot unaPropuesta = unaPropuestaDeUnPinoARoot();

        TemaParaProponerPinosARoot unTemaParaProponerPinos = new TemaParaProponerPinosARoot(unaPropuesta);

        assertThat(unTemaParaProponerPinos.propuestas()).containsExactly(unaPropuesta);
    }

    @Test
    public void sePuedeAgregarUnaPropuestaDePinoARoot() {
        PropuestaDePinoARoot unaPropuesta = unaPropuestaDeUnPinoARoot();
        PropuestaDePinoARoot otraPropuesta = unaPropuestaDeOtroPinoARoot();
        Collection<PropuestaDePinoARoot> propuestas = Arrays.asList(unaPropuesta, otraPropuesta);
        TemaParaProponerPinosARoot unTemaParaProponerPinos = new TemaParaProponerPinosARoot(unaPropuesta);

        unTemaParaProponerPinos.agregarPropuesta(otraPropuesta);

        assertThat(unTemaParaProponerPinos.propuestas()).containsExactlyElementsOf(propuestas);
    }

    @Test
    public void noSePuedeAgregarLaMismaPropuestaDePinoARootMasDeUnaVez() {
        PropuestaDePinoARoot unaPropuesta = unaPropuestaDeUnPinoARoot();
        TemaParaProponerPinosARoot unTemaParaProponerPinos = new TemaParaProponerPinosARoot(unaPropuesta);

        assertThatThrownBy(() -> {
            unTemaParaProponerPinos.agregarPropuesta(unaPropuesta);
        }).hasMessage(TemaParaProponerPinosARoot.PINO_YA_PROPUESTO_ERROR_MSG);
    }

    @Test
    public void noSePuedeProponerAlMismoPinoMasDeUnaVez() {
        PropuestaDePinoARoot unaPropuesta = unaPropuestaDeUnPinoARootSponsoreadoPor(unSponsor());
        PropuestaDePinoARoot otraPropuesta = unaPropuestaDeUnPinoARootSponsoreadoPor(otroSponsor());
        TemaParaProponerPinosARoot unTemaParaProponerPinos = new TemaParaProponerPinosARoot(unaPropuesta);

        assertThatThrownBy(() -> {
            unTemaParaProponerPinos.agregarPropuesta(otraPropuesta);
        }).hasMessage(TemaParaProponerPinosARoot.PINO_YA_PROPUESTO_ERROR_MSG);
    }

    private PropuestaDePinoARoot unaPropuestaDeUnPinoARootSponsoreadoPor(Usuario unSponsor) {
        return new PropuestaDePinoARoot(unPino(), unSponsor);
    }

    private PropuestaDePinoARoot unaPropuestaDeOtroPinoARoot() {
        return new PropuestaDePinoARoot(otroPino(), unSponsor());
    }

    private PropuestaDePinoARoot unaPropuestaDeUnPinoARoot() {
        return new PropuestaDePinoARoot(unPino(), unSponsor());
    }

    private Usuario unSponsor() {
        return Usuario.create("jorge", "usuario", "contra", "id", "email");
    }

    private Usuario otroSponsor() {
        return Usuario.create("carlos", "usuario", "contra", "id", "email");
    }

    private String unPino() {
        return "un pino";
    }

    private String otroPino() {
        return "otro pino";
    }
}
