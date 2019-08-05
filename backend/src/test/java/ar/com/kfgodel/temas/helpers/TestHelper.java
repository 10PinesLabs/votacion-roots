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

    public String unPino() {
        return "un pino";
    }

    public String otroPino() {
        return "otro pino";
    }

    public Usuario unUsuario() {
        return Usuario.create("jorge", "usuario", "contra", "id", "email");
    }

    public Usuario otroUsuario() {
        return Usuario.create("carlos", "usuario", "contra", "id", "email");
    }

    public PropuestaDePinoARoot unaPropuestaDeUnPinoARoot() {
        return new PropuestaDePinoARoot(unPino(), unUsuario());
    }

    public PropuestaDePinoARoot unaPropuestaDeOtroPinoARoot() {
        return new PropuestaDePinoARoot(otroPino(), unUsuario());
    }

    public PropuestaDePinoARoot unaPropuestaDeUnPinoARootSponsoreadoPor(Usuario unSponsor, TemaParaProponerPinosARootTest temaParaProponerPinosARootTest) {
        return new PropuestaDePinoARoot(unPino(), unSponsor);
    }

    public TemaDeReunion unTemaDeReunion() {
        return unTemaNoObligatorio();
    }

    public TemaDeReunion unTemaDeReunionConTitulo(String unTitulo) {
        TemaDeReunion unTema = TemaDeReunion.create();
        unTema.setTitulo(unTitulo);
        return unTema;
    }

    public String unTitulo() {
        return "Un título";
    }

    public String unaDescripcion() {
        return "Una descripción";
    }

    public DuracionDeTema unaDuracion() {
        return DuracionDeTema.CORTO;
    }

    public ObligatoriedadDeTema unaObligatoriedad() {
        return ObligatoriedadDeTema.OBLIGATORIO;
    }
}
