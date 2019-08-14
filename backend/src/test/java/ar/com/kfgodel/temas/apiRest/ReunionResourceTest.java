package ar.com.kfgodel.temas.apiRest;

import convention.persistent.*;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class ReunionResourceTest extends ResourceTest {

    @Test
    public void testProponerPinoComoRootAgregaUnTemaALaReunion() throws IOException {
        Long idReunion = persistentHelper.crearUnaReunion().getId();

        HttpResponse response = makeJsonPostRequest("reuniones/" + idReunion + "/propuestas", jsonDeUnaPropuesta());

        Reunion reunionActualizada = reunionService.get(idReunion);
        assertThatResponseStatusCodeIs(response, HttpStatus.SC_OK);
        assertThat(reunionActualizada.getTemasPropuestos()).hasSize(1);
    }

    @Test
    public void testProponerPinoComoRootAgregaUnaPropuestaParaElPino() throws IOException {
        Long idReunion = persistentHelper.crearUnaReunion().getId();

        String unNombre = "nombre";
        makeJsonPostRequest("reuniones/" + idReunion + "/propuestas", jsonDeUnaPropuestaPara(unNombre));

        Reunion reunionActualizada = reunionService.get(idReunion);
        TemaParaProponerPinosARoot temaParaProponerPinos = (TemaParaProponerPinosARoot) reunionActualizada.getTemasPropuestos().get(0);
        List<PropuestaDePinoARoot> propuestasDePino = temaParaProponerPinos.propuestas();

        assertThat(propuestasDePino).hasSize(1);
        assertThat(propuestasDePino).anyMatch(propuesta -> Objects.equals(propuesta.pino(), unNombre));
    }

    @Test
    public void testProponerPinoComoRootResponseConLaReunionActualizada() throws IOException {
        Long idReunion = persistentHelper.crearUnaReunion().getId();

        HttpResponse response = makeJsonPostRequest("reuniones/" + idReunion + "/propuestas", jsonDeUnaPropuesta());

        String responseBody = getResponseBody(response);
        JSONObject responseJson = new JSONObject(responseBody);

        assertThat(responseJson.get("id")).isEqualTo(idReunion.intValue());
        assertThat(responseJson.getJSONArray("temasPropuestos").length()).isEqualTo(1);
    }

    @Test
    public void testGetDeReunionDistingueLosTiposDeTema() throws IOException {
        Reunion unaReunion = persistentHelper.crearUnaReunionConTemas();

        HttpResponse response = makeGetRequest("reuniones/" + unaReunion.getId());

        JSONObject responseJson = new JSONObject(getResponseBody(response));
        JSONObject jsonDelTema = responseJson.getJSONArray("temasPropuestos").getJSONObject(0);
        assertThat(jsonDelTema.has("tipo")).isTrue();
    }

    @Test
    public void testGetDeReunionTieneAlTemaParaProponerPinosConLasPropuestas() throws IOException {
        Usuario unSponsor = unUsuarioPersistido();
        Reunion unaReunion = helper.unaReunion();
        String unPino = helper.unPino();
        unaReunion.proponerPinoComoRoot(unPino, unSponsor);
        reunionService.save(unaReunion);

        Long idReunion = unaReunion.getId();
        HttpResponse response = makeGetRequest("reuniones/" + idReunion);

        JSONObject responseJson = new JSONObject(getResponseBody(response));
        JSONObject jsonDelTema = responseJson.getJSONArray("temasPropuestos").getJSONObject(0);
        JSONObject jsonDeLaPropuesta = jsonDelTema.getJSONArray("propuestas").getJSONObject(0);
        assertThat(jsonDeLaPropuesta.getString("pino")).isEqualTo(unPino);
    }

    private Usuario unUsuarioPersistido() {
        return usuarioService.getAll().get(0);
    }

    private String jsonDeUnaPropuesta() {
        return jsonDeUnaPropuestaPara(helper.unPino());
    }

    private String jsonDeUnaPropuestaPara(String unNombre) {
        return new JSONObject().put("pino", unNombre).toString();
    }
}
