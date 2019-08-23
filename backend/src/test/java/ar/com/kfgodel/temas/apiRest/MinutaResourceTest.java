package ar.com.kfgodel.temas.apiRest;

import convention.persistent.Minuta;
import convention.persistent.Reunion;
import convention.persistent.Usuario;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class MinutaResourceTest extends ResourceTest {

    @Test
    public void testCuandoUnUsuarioCreaUnaMinutaPasaASerSuMinuteador() throws IOException {
        Reunion unaReunion = reunionService.save(helper.unaReunionCerrada());

        makeGetRequest("minuta/reunion/" + unaReunion.getId());

        Minuta minutaCreada = minutaService.getUltimaMinuta().get();
        assertThat(minutaCreada.getMinuteador()).isEqualTo(getAuthenticatedUser());
    }

    @Test
    public void testCuandoUnUsuarioHaceGetDeUnaMinutaNoPasaASerSuMinuteador() throws IOException {
        Reunion unaReunion = reunionService.save(helper.unaReunionMinuteada());
        Minuta unaMinuta = Minuta.create(unaReunion);
        Usuario unUsuario = usuarioService.save(helper.unUsuario());
        unaMinuta.setMinuteador(unUsuario);
        minutaService.save(unaMinuta);

        makeGetRequest("minuta/reunion/" + unaReunion.getId());

        Minuta minutaCreada = minutaService.getUltimaMinuta().get();
        assertThat(minutaCreada.getMinuteador()).isNotEqualTo(getAuthenticatedUser());
    }
}
