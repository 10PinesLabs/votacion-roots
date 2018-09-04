package ar.com.kfgodel.temas.domain;

import convention.persistent.Minuta;
import convention.persistent.Reunion;
import convention.persistent.TemaDeReunion;
import convention.persistent.Usuario;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by sandro on 06/07/17.
 */
public class MinutaTest {

    @Test
    public void test01UnaMinutaNuevaNoTieneAsistentesSiNingunTemaFueVotado(){
        Reunion reunion = Reunion.create(LocalDate.of(2017, 06, 26));
        Minuta minuta = Minuta.create(reunion);
        assertThat(minuta.getAsistentes()).hasSize(0);
    }

    @Test
    public void test02UnaMinutaNuevaTieneComoAsistentesALosVotantesDeLosTemas(){
        Reunion reunion = Reunion.create(LocalDate.of(2017, 06, 26));
        TemaDeReunion unTema = TemaDeReunion.create();
        Usuario unVotante = Usuario.create("Juan","Juan","juan","88","juan@10pines.com");
        unTema.setInteresados(Arrays.asList(unVotante));
        reunion.setTemasPropuestos(Arrays.asList(unTema));
        Minuta minuta = Minuta.create(reunion);

        assertThat(minuta.getAsistentes()).contains(unVotante);
    }
}
