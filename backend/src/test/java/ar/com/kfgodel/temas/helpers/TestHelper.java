package ar.com.kfgodel.temas.helpers;

import ar.com.kfgodel.temas.domain.TemaParaProponerPinosARootTest;
import convention.persistent.*;

import java.time.LocalDate;

/**
 * Created by sandro on 30/06/17.
 */
public class TestHelper {

    public TemaDeReunion unTemaObligatorio() {
        TemaDeReunion tema = TemaDeReunion.create();
        tema.setObligatoriedad(ObligatoriedadDeTema.OBLIGATORIO);
        return tema;
    }

    public TemaDeReunion unTemaNoObligatorio() {
        TemaDeReunion tema = TemaDeReunion.create();
        tema.setObligatoriedad(ObligatoriedadDeTema.NO_OBLIGATORIO);
        return tema;
    }

    public TemaDeReunion unTemaAPartirDeUnTemaGeneral() {
        Reunion reunion = Reunion.create(LocalDate.of(2017, 06, 26));
        TemaGeneral temaGeneral = new TemaGeneral();
        return temaGeneral.generarTemaPara(reunion);
    }

    public Reunion unaReunion() {
        return Reunion.create(LocalDate.of(2017, 06, 16));
    }

    public Usuario unUsuario() {
        return new Usuario();
    }

    public String unPino() {
        return "un pino";
    }

    public String otroPino() {
        return "otro pino";
    }

    public Usuario unSponsor() {
        return Usuario.create("jorge", "usuario", "contra", "id", "email");
    }

    public Usuario otroSponsor() {
        return Usuario.create("carlos", "usuario", "contra", "id", "email");
    }

    public PropuestaDePinoARoot unaPropuestaDeUnPinoARoot() {
        return new PropuestaDePinoARoot(unPino(), unSponsor());
    }

    public PropuestaDePinoARoot unaPropuestaDeOtroPinoARoot() {
        return new PropuestaDePinoARoot(otroPino(), unSponsor());
    }

    public PropuestaDePinoARoot unaPropuestaDeUnPinoARootSponsoreadoPor(Usuario unSponsor, TemaParaProponerPinosARootTest temaParaProponerPinosARootTest) {
        return new PropuestaDePinoARoot(unPino(), unSponsor);
    }

    public TemaParaProponerPinosARoot unTemaParaProponerPinosARoot() {
        return new TemaParaProponerPinosARoot(unaPropuestaDeUnPinoARoot());
    }
}
