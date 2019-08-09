package ar.com.kfgodel.temas.apiRest;

import ar.com.kfgodel.temas.helpers.TestHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import convention.persistent.*;
import convention.rest.api.tos.TemaDeReunionTo;
import convention.rest.api.tos.TemaEnCreacionTo;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class TemaDeReunionResourceTest extends ResourceTest {

    public static final String CAMPO_DE_TIPO = "tipo";

    private TestHelper helper = new TestHelper();

    @Test
    public void testGetDeTemaDeReunionContieneElTipoDeTemaParaTemasParaProponerPinosARoot() throws IOException {
        Long idTema = crearUnTemaParaProponerPinosARoot().getId();

        HttpResponse response = makeGetRequest("temas/" + idTema);

        JSONObject responseJson = new JSONObject(getResponseBody(response));
        assertThat(responseJson.getString(CAMPO_DE_TIPO)).isEqualTo("proponerPinos");
    }

    @Test
    public void testGetDeTemaDeReunionContieneElTipoDeTemaParaTemasDeReunionConDescripcion() throws IOException {
        Long idTema = crearUnTemaDeReunionConDescripcion().getId();

        HttpResponse response = makeGetRequest("temas/" + idTema);

        JSONObject responseJson = new JSONObject(getResponseBody(response));
        assertThat(responseJson.getString(CAMPO_DE_TIPO)).isEqualTo("conDescripcion");
    }

    @Test
    public void testSePuedeEliminarUnTemaParaProponerPinosARoot() throws IOException {
        Usuario unUsuario = unUsuarioPersistido();
        TemaParaProponerPinosARoot unTemaParaProponerPinos = TemaParaProponerPinosARoot.create(unUsuario);
        PropuestaDePinoARoot unaPropuesta = new PropuestaDePinoARoot(helper.unPino(), unUsuarioPersistido());
        unTemaParaProponerPinos.agregarPropuesta(unaPropuesta);

        Long idTema = temaService.save(unTemaParaProponerPinos).getId();
        makeDeleteRequest("temas/" + idTema);

        assertThat(temaService.getAll()).hasSize(0);
    }

    @Test
    public void testSePuedeVotarUnTemaParaProponerPinosARoot() throws IOException {
        TemaParaProponerPinosARoot unTemaParaProponerPinos = crearUnTemaParaProponerPinosARoot();
        Long idTema = unTemaParaProponerPinos.getId();

        makeGetRequest("temas/votar/" + idTema);

        TemaDeReunion temaActualizado = temaService.get(idTema);
        assertThat(temaActualizado.getCantidadDeVotos()).isEqualTo(1);
    }

    @Test
    public void testNoSePuedeAgregarUnTemaConElTituloDeUnoParaProponerPinosARoot() throws IOException {
        TemaEnCreacionTo unTemaEnCreacionTo = new TemaEnCreacionTo();
        unTemaEnCreacionTo.setTitulo(TemaParaProponerPinosARoot.TITULO);

        String jsonDelTema = convertirAJsonString(unTemaEnCreacionTo);
        HttpResponse response = makeJsonPostRequest("temas", jsonDelTema);

        assertThatResponseStatusCodeIs(response, HttpStatus.SC_CONFLICT);
        assertThat(temaService.getAll()).hasSize(0);
    }

    @Test
    public void testNoSePuedeCambiarElTituloDeUnTemaAlDeUnoParaProponerPinosARoot() throws IOException {
        String tituloDelTema = helper.unTitulo();
        TemaDeReunion unTemaDeReunion = helper.unTemaDeReunionConTitulo(tituloDelTema);
        temaService.save(unTemaDeReunion);
        String unNuevoTitulo = TemaParaProponerPinosARoot.TITULO;
        unTemaDeReunion.setTitulo(unNuevoTitulo);
        Long idDelTema = unTemaDeReunion.getId();

        String jsonDelTemaConNuevoTitulo = convertirAJsonString(convertirATo(unTemaDeReunion));
        HttpResponse response = makeJsonPutRequest("temas/" + idDelTema, jsonDelTemaConNuevoTitulo);

        TemaDeReunion temaActualizado = temaService.get(idDelTema);
        assertThatResponseStatusCodeIs(response, HttpStatus.SC_CONFLICT);
        assertThat(temaActualizado.getTitulo()).isEqualTo(tituloDelTema);
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
