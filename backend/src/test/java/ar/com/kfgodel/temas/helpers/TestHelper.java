package ar.com.kfgodel.temas.helpers;

import convention.persistent.*;

import java.time.LocalDate;

/**
 * Created by sandro on 30/06/17.
 */
public class TestHelper {

    public TemaDeReunion nuevoTemaObligatorio(){
        TemaDeReunion tema = TemaDeReunion.create();
        tema.setObligatoriedad(ObligatoriedadDeTema.OBLIGATORIO);
        return tema;
    }

    public TemaDeReunion nuevoTemaNoObligatorio(){
        TemaDeReunion tema = TemaDeReunion.create();
        tema.setObligatoriedad(ObligatoriedadDeTema.NO_OBLIGATORIO);
        return tema;
    }

    public TemaDeReunion nuevoTemaAPartirDeUnTemaGeneral(){
        Reunion reunion = Reunion.create(LocalDate.of(2017, 06, 26));
        TemaGeneral temaGeneral = new TemaGeneral();
        return temaGeneral.generarTemaPara(reunion);
    }
}
