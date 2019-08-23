package ar.com.kfgodel.temas.apiRest;

import convention.persistent.Minuta;
import convention.persistent.Reunion;
import convention.persistent.TemaDeMinuta;
import convention.persistent.Usuario;
import convention.rest.api.tos.TemaDeMinutaTo;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

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

    @Test
    public void testCuandoUnUsuarioModificaUnTemaDeMinutaPasaASerElMinuteadorDeElla() throws IOException {
        Reunion unaReunion = reunionService.save(helper.unaReunionMinuteadaConTemas());
        Minuta unaMinuta = Minuta.create(unaReunion);
        Usuario unUsuario = usuarioService.save(helper.unUsuario());
        unaMinuta.setMinuteador(unUsuario);
        unaMinuta = minutaService.save(unaMinuta);

        TemaDeMinuta unTemaDeMinuta = unaMinuta.getTemas().get(0);
        unTemaDeMinuta.setActionItems(new ArrayList<>());
        TemaDeMinutaTo toDelTemaDeMinuta = convertirATo(unTemaDeMinuta);
        HttpResponse response =
                makeJsonPutRequest("temaDeMinuta/" + unTemaDeMinuta.getId(), convertirAJsonString(toDelTemaDeMinuta));

        Minuta minutaActualizada = minutaService.get(unaMinuta.getId());
        assertThatResponseStatusCodeIs(response, HttpStatus.SC_OK);
        assertThat(minutaActualizada.getMinuteador()).isEqualTo(getAuthenticatedUser());
    }
}
