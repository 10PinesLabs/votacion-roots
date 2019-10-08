package ar.com.kfgodel.temas.domain;

import ar.com.kfgodel.temas.helpers.TestHelper;
import convention.persistent.DuracionDeTema;
import convention.persistent.ObligatoriedadDeTema;
import convention.persistent.TemaDeReunionConDescripcion;
import convention.persistent.Usuario;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TemaDeReunionConDescripcionTest {

    private TestHelper helper = new TestHelper();

    @Test
    public void testSePuedeCrearUnTemaDeReunionConDescripcion() {
        Usuario autor = helper.unUsuario();
        String titulo = helper.unTitulo();
        String descripcion = helper.unaDescripcion();
        DuracionDeTema duracion = helper.unaDuracion();
        ObligatoriedadDeTema obligatoriedad = helper.unaObligatoriedad();

        TemaDeReunionConDescripcion tema =
                TemaDeReunionConDescripcion.create(autor, duracion, obligatoriedad, titulo, descripcion);

        assertThat(tema.getAutor()).isEqualTo(autor);
        assertThat(tema.getDuracion()).isEqualTo(duracion);
        assertThat(tema.getObligatoriedad()).isEqualTo(obligatoriedad);
        assertThat(tema.getTitulo()).isEqualTo(titulo);
        assertThat(tema.getDescripcion()).isEqualTo(descripcion);
    }
}
