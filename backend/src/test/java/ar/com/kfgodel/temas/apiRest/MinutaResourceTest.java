package ar.com.kfgodel.temas.apiRest;

import convention.persistent.*;
import convention.rest.api.tos.TemaDeMinutaTo;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class MinutaResourceTest extends ResourceTest {

    @Test
    public void cuandoUnUsuarioCreaUnaMinutaPasaASerSuMinuteador() throws IOException {
        Reunion unaReunion = reunionService.save(helper.unaReunionCerrada());

        makeGetRequest("minuta/reunion/" + unaReunion.getId());

        Minuta minutaCreada = minutaService.getUltimaMinuta().get();
        assertThat(minutaCreada.getMinuteador()).isEqualTo(getAuthenticatedUser());
    }

    @Test
    public void cuandoUnUsuarioHaceGetDeUnaMinutaNoPasaASerSuMinuteador() throws IOException {
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
    public void cuandoUnUsuarioModificaUnTemaDeMinutaPasaASerElMinuteadorDeElla() throws IOException {
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
        assertThat(minutaActualizada.getMinuteador()).isEqualTo(getAuthenticatedUser());
        assertThatResponseStatusCodeIs(response, HttpStatus.SC_OK);
        JSONObject temaDeMinuta = new JSONObject(getResponseBody(response));
        assertThat(temaDeMinuta.isNull("id")).isFalse();
    }

    @Test
    public void cuandoUnUsuarioHaceGetDeUnaMinutaConTemaParaRepasarActionItemsElRequestEsExitoso() throws IOException {
        TemaParaRepasarActionItems temaParaRepasarActionItems =
            persistentHelper.crearUnTemaDeReunionParaRepasarActionItems();
        Reunion reunion = temaParaRepasarActionItems.getReunion();
        Minuta unaMinuta = Minuta.create(reunion);
        unaMinuta.setMinuteador(persistentHelper.crearUnUsuario());
        minutaService.save(unaMinuta);

        HttpResponse response = makeGetRequest("minuta/reunion/" + reunion.getId());

        assertThat(getStatusCode(response)).isEqualTo(HttpStatus.SC_OK);
    }
}
