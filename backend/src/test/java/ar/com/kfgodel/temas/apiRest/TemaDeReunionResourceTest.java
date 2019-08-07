package ar.com.kfgodel.temas.apiRest;

import ar.com.kfgodel.temas.helpers.TestHelper;
import convention.persistent.TemaDeReunionConDescripcion;
import convention.persistent.TemaParaProponerPinosARoot;
import convention.persistent.Usuario;
import convention.rest.api.TemaDeReunionResource;
import convention.services.TemaService;
import convention.services.UsuarioService;
import org.apache.http.HttpResponse;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class TemaDeReunionResourceTest extends ResourceTest {

    public static final String CAMPO_DE_TIPO = "tipo";

    private TestHelper helper = new TestHelper();
    private TemaService temaService;
    private UsuarioService usuarioService;

    @Before
    public void setUp() throws IOException {
        super.setUp();
        TemaDeReunionResource.create(getInjector());
        temaService = getInjector().getImplementationFor(TemaService.class).get();
        usuarioService = getInjector().createInjected(UsuarioService.class);
    }

    @Test
    public void testGetDeTemaDeReunionDistingueTemasParaProponerPinosARoot() throws IOException {
        Long idTema = crearUnTemaParaProponerPinosARoot().getId();

        HttpResponse response = makeGetRequest("temas/" + idTema);

        JSONObject responseJson = new JSONObject(getResponseBody(response));
        assertThat(responseJson.getString(CAMPO_DE_TIPO)).isEqualTo("proponerPinos");
    }

    @Test
    public void testGetDeTemaDeReunionDistingueTemasDeReunionConDescripcion() throws IOException {
        Long idTema = crearUnTemaDeReunionConDescripcion().getId();

        HttpResponse response = makeGetRequest("temas/" + idTema);

        JSONObject responseJson = new JSONObject(getResponseBody(response));
        assertThat(responseJson.getString(CAMPO_DE_TIPO)).isEqualTo("conDescripcion");
    }

    private TemaDeReunionConDescripcion crearUnTemaDeReunionConDescripcion() {
        TemaDeReunionConDescripcion unTemaConDescripcion = new TemaDeReunionConDescripcion();
        temaService.save(unTemaConDescripcion);
        return unTemaConDescripcion;
    }

    private TemaParaProponerPinosARoot crearUnTemaParaProponerPinosARoot() {
        Usuario unUsuario = unUsuarioPersistido();
        TemaParaProponerPinosARoot unTemaParaProponerPinos = TemaParaProponerPinosARoot.create(unUsuario);
        temaService.save(unTemaParaProponerPinos);
        return unTemaParaProponerPinos;
    }

    private Usuario unUsuarioPersistido() {
        return usuarioService.getAll().get(0);
    }

}
