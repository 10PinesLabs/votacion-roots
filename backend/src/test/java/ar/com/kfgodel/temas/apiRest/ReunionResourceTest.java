package ar.com.kfgodel.temas.apiRest;

import ar.com.kfgodel.temas.helpers.TestHelper;
import convention.persistent.PropuestaDePinoARoot;
import convention.persistent.Reunion;
import convention.persistent.TemaParaProponerPinosARoot;
import convention.rest.api.ReunionResource;
import convention.services.ReunionService;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class ReunionResourceTest extends ResourceTest {

    private TestHelper helper = new TestHelper();
    private ReunionService reunionService;

    @Before
    public void setUp() throws IOException {
        super.setUp();
        ReunionResource.create(getInjector());
        reunionService = getInjector().getImplementationFor(ReunionService.class).get();
    }

    @Test
    public void testProponerPinoComoRootAgregaUnTemaALaReunion() throws IOException {
        Long idReunion = crearUnaReunion().getId();

        HttpResponse response = makeJsonPostRequest("reuniones/" + idReunion + "/propuestas", jsonDeUnaPropuesta());

        Reunion reunionActualizada = reunionService.get(idReunion);
        assertThatResponseStatusCodeIs(getStatusCode(response), HttpStatus.SC_CREATED);
        assertThat(reunionActualizada.getTemasPropuestos()).hasSize(1);
    }

    @Test
    public void testProponerPinoComoRootAgregaUnaPropuestaParaElPino() throws IOException {
        Long idReunion = crearUnaReunion().getId();

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
        Long idReunion = crearUnaReunion().getId();

        HttpResponse response = makeJsonPostRequest("reuniones/" + idReunion + "/propuestas", jsonDeUnaPropuesta());

        String responseBody = getResponseBody(response);
        JSONObject responseJson = new JSONObject(responseBody);

        assertThat(responseJson.get("id")).isEqualTo(idReunion.intValue());
        assertThat(responseJson.getJSONArray("temasPropuestos").length()).isEqualTo(1);
    }

    private Reunion crearUnaReunion() {
        Reunion reunion = helper.unaReunion();
        reunionService.save(reunion);
        return reunion;
    }

    private String jsonDeUnaPropuesta() {
        return jsonDeUnaPropuestaPara(helper.unPino());
    }

    private String jsonDeUnaPropuestaPara(String unNombre) {
        return new JSONObject().put("pino", unNombre).toString();
    }
}
