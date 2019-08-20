package ar.com.kfgodel.temas.domain;

import ar.com.kfgodel.temas.helpers.TestHelper;
import convention.persistent.Minuta;
import convention.persistent.TemaDeReunion;
import convention.persistent.TemaParaRepasarActionItems;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TemaParaRepasarActionItemsTest {

    private TestHelper helper = new TestHelper();

    @Test
    public void testUnTemaParaRepasarActionItemsTieneLoMismoQueElTemaAPartirDelCualFueCreado() {
        Minuta unaMinuta = helper.unaMinuta();
        TemaDeReunion unTemaDeReunion = helper.unTemaDeReunion();

        TemaParaRepasarActionItems unTemaParaRepasarActionItems = TemaParaRepasarActionItems.create(unaMinuta, unTemaDeReunion);

        assertThat(unTemaParaRepasarActionItems.getTitulo()).isEqualTo(unTemaDeReunion.getTitulo());
        assertThat(unTemaParaRepasarActionItems.getDescripcion()).isEqualTo(unTemaDeReunion.getDescripcion());
        assertThat(unTemaParaRepasarActionItems.getObligatoriedad()).isEqualTo(unTemaDeReunion.getObligatoriedad());
        assertThat(unTemaParaRepasarActionItems.getReunion()).isEqualTo(unTemaDeReunion.getReunion());
        assertThat(unTemaParaRepasarActionItems.getDuracion()).isEqualTo(unTemaDeReunion.getDuracion());
        assertThat(unTemaParaRepasarActionItems.getAutor()).isEqualTo(unTemaDeReunion.getAutor());
        assertThat(unTemaParaRepasarActionItems.getInteresados()).containsExactlyElementsOf(unTemaDeReunion.getInteresados());
    }

    @Test
    public void testUnTemaParaRepasarActionItemsTieneLosTemasDeMinutaDeLaMinutaAPartirDelCualFueCreado() {
        Minuta unaMinuta = helper.unaMinuta();
        TemaDeReunion unTemaDeReunion = helper.unTemaDeReunion();

        TemaParaRepasarActionItems unTemaParaRepasarActionItems = TemaParaRepasarActionItems.create(unaMinuta, unTemaDeReunion);

        assertThat(unTemaParaRepasarActionItems.getTemasParaRepasar()).containsExactlyElementsOf(unaMinuta.getTemas());
    }
}
