package ar.com.kfgodel.temas.apiRest;

import convention.persistent.*;
import convention.rest.api.tos.TemaDeMinutaTo;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class TemasParaExportarResourceTest extends ResourceTest {

    @Override
    protected String apiVersion() {
        return "v2";
    }

    @Test
    public void patchDeTemaDeMintaParaTemaInexistenteRespondeConNotFound() throws IOException {
        TemaDeMinutaTo temaDeMinutaTo = unPatchRequestDeTemaDeMinuta();

        HttpResponse response = makePatchRequest(pathDePatchDeTemaDeMinuta(123L), convertirAJsonString(temaDeMinutaTo));

        assertThat(getStatusCode(response)).isEqualTo(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void patchDeTemaDeMinutaCuandoNoHayMinutaRespondeConNotFound() throws IOException {
        TemaDeReunion unTemaDeReunion = helper.unTemaDeReunionDe(unUsuarioPersistido());
        Reunion reunion = helper.unaReunionConTemas(unTemaDeReunion);
        reunionService.save(reunion);

        HttpResponse response = makePatchRequest(pathDePatchDeTemaDeMinuta(unTemaDeReunion.getId()), convertirAJsonString(unPatchRequestDeTemaDeMinuta()));

        assertThat(getStatusCode(response)).isEqualTo(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void patchDeTemaDeMinutaConRequestInvalidoRespondeConBadRequest() throws IOException {
        TemaDeReunion unTemaDeReunion = helper.unTemaDeReunionDe(unUsuarioPersistido());
        reunionService.save(helper.unaReunionConTemas(unTemaDeReunion));

        TemaDeMinutaTo requestInvalido = new TemaDeMinutaTo();
        HttpResponse response = makePatchRequest(pathDePatchDeTemaDeMinuta(unTemaDeReunion.getId()), convertirAJsonString(requestInvalido));

        assertThat(getStatusCode(response)).isEqualTo(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void patchDeFueTratadoDeTemaDeMinutaActualizaLaMinuta() throws IOException {
        Usuario usuario = unUsuarioPersistido();
        TemaDeReunion unTemaDeReunion = helper.unTemaDeReunionDe(usuario);
        TemaDeReunion otroTemaDeReunion = helper.unTemaDeReunionDe(usuario);
        Reunion reunion = reunionService.save(helper.unaReunionConTemas(unTemaDeReunion, otroTemaDeReunion));
        Minuta minuta = minutaService.save(Minuta.create(reunion));
        TemaDeMinuta unTemaDeMinuta = temaDeMinutaDe(minuta, unTemaDeReunion);
        TemaDeMinuta otroTemaDeMinuta = temaDeMinutaDe(minuta, otroTemaDeReunion);

        TemaDeMinutaTo temaDeMinutaTo = new TemaDeMinutaTo();
        boolean fueTratado = true;
        temaDeMinutaTo.setFueTratado(fueTratado);
        HttpResponse response = makePatchRequest(pathDePatchDeTemaDeMinuta(unTemaDeReunion.getId()), convertirAJsonString(temaDeMinutaTo));

        assertThat(getStatusCode(response)).isEqualTo(HttpStatus.SC_NO_CONTENT);
        assertThat(temaDeMinutaService.get(unTemaDeMinuta.getId()).getFueTratado()).isEqualTo(fueTratado);
        assertThat(temaDeMinutaService.get(otroTemaDeMinuta.getId()).getFueTratado()).isEqualTo(false);
    }

    @Test
    public void patchDeTemaDeMinutaSinApiKeyRespondeConUnauthorized() throws IOException {
        HttpResponse response = makePatchRequest(pathDePatchDeTemaDeMinutaSinApiKey(123L), "{}");

        assertThat(getStatusCode(response)).isEqualTo(HttpStatus.SC_UNAUTHORIZED);
    }

    private String pathDePatchDeTemaDeMinuta(Long idDeTemaDeReunion) {
        return pathDePatchDeTemaDeMinutaSinApiKey(idDeTemaDeReunion) + "?apiKey=" + environment.apiKey();
    }

    private String pathDePatchDeTemaDeMinutaSinApiKey(Long idDeTemaDeReunion) {
        return "temas/" + idDeTemaDeReunion + "/temaDeMinuta";
    }

    private TemaDeMinutaTo unPatchRequestDeTemaDeMinuta() {
        TemaDeMinutaTo temaDeMinutaTo = new TemaDeMinutaTo();
        temaDeMinutaTo.setFueTratado(true);
        return temaDeMinutaTo;
    }

    private TemaDeMinuta temaDeMinutaDe(Minuta minuta, TemaDeReunion temaDeReunion) {
        return minuta.getTemas().stream()
            .filter(temaDeMinuta -> temaDeMinuta.getTema().equals(temaDeReunion))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("El tema de reunión no tiene tema de minuta asociado"));
    }
}