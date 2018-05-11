package ar.com.kfgodel.temas.domain;

import convention.persistent.ObligatoriedadDeTema;
import convention.persistent.Reunion;
import convention.persistent.TemaDeReunion;
import convention.persistent.TemaGeneral;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sandro on 03/07/17.
 */
public class TemaGeneralTest {

    @Test
    public void test01UnTemaGeneralPuedeGenerarUnTemaDeReunionParaUnaReunion(){
        TemaGeneral temaGeneral = new TemaGeneral();
        Reunion reunion = new Reunion();

        TemaDeReunion unTema = temaGeneral.generarTemaPara(reunion);

        Assert.assertEquals(reunion, unTema.getReunion());
    }

    @Test
    public void test02ElTemaDeReunionGeneradoPorUnTemaGeneralEsObligatorio(){
        TemaGeneral temaGeneral = new TemaGeneral();
        Reunion reunion = new Reunion();
        TemaDeReunion unTema = temaGeneral.generarTemaPara(reunion);

        Assert.assertEquals(ObligatoriedadDeTema.OBLIGATORIO, unTema.getObligatoriedad());
    }

    @Test
    public void test03SePuedenAgregarTemasGeneralesAUnaReunionParaQueLosAgregueASusTemas(){
        TemaGeneral temaGeneral = new TemaGeneral();
        List<TemaGeneral> temasGenerales = Arrays.asList(temaGeneral);
        Reunion reunion = Reunion.create(LocalDate.of(2017, 06, 26));
        reunion.agregarTemasGenerales(temasGenerales);

        Assert.assertEquals(1, reunion.getTemasPropuestos().size());
    }

}
