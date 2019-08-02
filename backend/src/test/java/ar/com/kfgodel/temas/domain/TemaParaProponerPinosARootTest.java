package ar.com.kfgodel.temas.domain;

import convention.persistent.*;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    public void sePuedeAgregarUnTemaParaProponerPinosARootAUnaReunion() {
        Reunion reunion = Reunion.create(LocalDate.of(2019, 1, 1));
        TemaParaProponerPinosARoot unTemaParaProponerPinos = unTemaParaProponerPinosARoot();

        reunion.agregarTema(unTemaParaProponerPinos);

        assertThat(reunion.getTemasPropuestos()).containsExactly(unTemaParaProponerPinos);
    }

    @Test
    public void losTemasParaProponerPinosARootTienenUnTituloFijo() {
        TemaParaProponerPinosARoot unTemaParaProponerPinos = unTemaParaProponerPinosARoot();

        assertThat(unTemaParaProponerPinos.getTitulo()).isEqualTo(TemaParaProponerPinosARoot.TITULO);
    }

    @Test
    public void losTemasParaProponerPinosARootNoSonObligatorios() {
        TemaParaProponerPinosARoot unTemaParaProponerPinos = unTemaParaProponerPinosARoot();

        assertThat(unTemaParaProponerPinos.getObligatoriedad()).isEqualTo(ObligatoriedadDeTema.OBLIGATORIO);
    }

    @Test
    public void losTemasParaProponerPinosARootTienenDuracionCorta() {
        TemaParaProponerPinosARoot unTemaParaProponerPinos = unTemaParaProponerPinosARoot();

        assertThat(unTemaParaProponerPinos.getDuracion()).isEqualTo(DuracionDeTema.CORTO);
    }

    private TemaParaProponerPinosARoot unTemaParaProponerPinosARoot() {
        return new TemaParaProponerPinosARoot(unaPropuestaDeUnPinoARoot());
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
