package ar.com.kfgodel.temas.apiRest;

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

    @Test
    public void testUnaRePropuestaSeCreaCreandoUnTemaConUnaPropuestaOriginal() throws IOException {
        TemaDeReunionConDescripcion unaPropuestaOriginal = crearUnTemaDeReunionConDescripcion();
        TemaEnCreacionTo unTemaEnCreacionTo = helper.unTemaEnCreacionTo(reunionService.save(helper.unaReunion()));
        unTemaEnCreacionTo.setIdDePropuestaOriginal(unaPropuestaOriginal.getId());

        HttpResponse response = makeJsonPostRequest("temas/", convertirAJsonString(unTemaEnCreacionTo));

        Long idDelTemaCreado = new JSONObject(getResponseBody(response)).getLong("id");
        TemaDeReunion temaCreado = temaService.get(idDelTemaCreado);
        assertThat(temaCreado.propuestaOriginal().get()).isEqualTo(unaPropuestaOriginal);
    }

    @Test
    public void testGetDeUnaRePropuestaContieneElIdDeLaPropuestaOriginal() throws IOException {
        TemaDeReunionConDescripcion unaPropuestaOriginal = crearUnTemaDeReunionConDescripcion();
        Reunion unaReunion = reunionService.save(helper.unaReunion());
        TemaDeReunion unTema = temaService.save(helper.unaRePropuestaDeParaReunion(unaPropuestaOriginal, unaReunion));

        HttpResponse response = makeGetRequest("temas/" + unTema.getId());

        JSONObject jsonResponse = new JSONObject(getResponseBody(response));
        assertThat(jsonResponse.getLong("idDePropuestaOriginal")).isEqualTo(unaPropuestaOriginal.getId());
    }

    @Test
    public void testCrearUnTemaConUnaRePropuestaComoPropuestaOriginalRetornaUnBadRequest() throws IOException {
        TemaDeReunion unaPropuestaOriginal = temaService.save(helper.unTemaDeReunion());
        TemaDeReunion unaRePropuesta = temaService.save(helper.unaRePropuestaDe(unaPropuestaOriginal));

        TemaEnCreacionTo unTemaEnCreacionTo = helper.unTemaEnCreacionTo(reunionService.save(helper.unaReunion()));
        unTemaEnCreacionTo.setIdDePropuestaOriginal(unaRePropuesta.getId());

        HttpResponse response = makeJsonPostRequest("temas/", convertirAJsonString(unTemaEnCreacionTo));

        assertThatResponseStatusCodeIs(response, HttpStatus.SC_BAD_REQUEST);
        assertThat(temaService.getAll()).containsExactly(unaPropuestaOriginal, unaRePropuesta);
    }

    @Test
    public void testNoSePuedeCrearMasDeUnTemaConLaMismaPropuestaOriginalParaLaMismaReunion() throws IOException {
        TemaDeReunion unaPropuestaOriginal = temaService.save(helper.unTemaDeReunion());
        Reunion unaReunion = reunionService.save(helper.unaReunion());
        TemaDeReunion unTema = temaService.save(
            helper.unaRePropuestaDeParaReunion(unaPropuestaOriginal, unaReunion));
        reunionService.save(unaReunion);

        TemaEnCreacionTo unTemaEnCreacionTo = helper.unTemaEnCreacionTo(unaReunion);
        unTemaEnCreacionTo.setIdDePropuestaOriginal(unaPropuestaOriginal.getId());
        HttpResponse response = makeJsonPostRequest("temas/", convertirAJsonString(unTemaEnCreacionTo));

        assertThatResponseStatusCodeIs(response, HttpStatus.SC_CONFLICT);
        assertThat(temaService.getAll()).containsExactly(unaPropuestaOriginal, unTema);
    }

    @Test
    public void testSePuedeModificarUnaRePropuesta() throws IOException {
        TemaDeReunion unaPropuestaOriginal = temaService.save(helper.unTemaDeReunion());
        Reunion unaReunion = reunionService.save(helper.unaReunion());
        TemaDeReunion unTema = temaService.save(
            helper.unaRePropuestaDeParaReunion(unaPropuestaOriginal, unaReunion));

        TemaDeReunionTo toDelTema = convertirATo(unTema);
        String unNuevoTitulo = "Un nuevo título";
        toDelTema.setTitulo(unNuevoTitulo);
        HttpResponse response = makeJsonPutRequest("temas/" + unTema.getId(), convertirAJsonString(toDelTema));

        TemaDeReunion temaActualizado = temaService.get(unTema.getId());
        assertThatResponseStatusCodeIs(response, HttpStatus.SC_OK);
        assertThat(temaActualizado.getTitulo()).isEqualTo(unNuevoTitulo);
    }

    @Test
    public void testCuandoSeEliminaUnaPropuestaOriginalSusRePropuestasDejanDeSerRePropuestas() throws IOException {
        TemaDeReunion unaPropuestaOriginal = temaService.save(helper.unTemaDeReunion());
        Reunion unaReunion = reunionService.save(helper.unaReunion());
        TemaDeReunion unTema = temaService.save(
            helper.unaRePropuestaDeParaReunion(unaPropuestaOriginal, unaReunion));

        HttpResponse response = makeDeleteRequest("temas/" + unaPropuestaOriginal.getId());

        assertThatResponseStatusCodeIs(response, HttpStatus.SC_NO_CONTENT);
        assertThat(temaService.getAll()).doesNotContain(unaPropuestaOriginal);
        assertThat(temaService.get(unTema.getId()).getEsRePropuesta()).isFalse();
    }

    @Test
    public void testGetDeTemasDeReunionContieneLaFechaDeLaPropuestaOriginal() throws IOException {
        Reunion unaReunion = reunionService.save(helper.unaReunion());
        TemaDeReunion unaPropuestaOriginal = temaService.save(helper.unTemaDeReunion(unaReunion));
        TemaDeReunion unTema = temaService.save(helper.unaRePropuestaDe(unaPropuestaOriginal));

        HttpResponse response = makeGetRequest("temas/" + unTema.getId());

        JSONObject jsonResponse = new JSONObject(getResponseBody(response));
        String fechaDeLaPropuestaOriginal = convertirA(unTema.getFechaDePropuestaOriginal(), String.class);
        assertThat(jsonResponse.getString("fechaDePropuestaOriginal")).isEqualTo(fechaDeLaPropuestaOriginal);
    }

    @Test
    public void testGetDeTemasDeReunionDiceSiEsUnaRePropuesta() throws IOException {
        TemaDeReunion unTema = temaService.save(helper.unTemaDeReunion());

        HttpResponse response = makeGetRequest("temas/" + unTema.getId());

        JSONObject jsonResponse = new JSONObject(getResponseBody(response));
        assertThat(jsonResponse.getBoolean("esRePropuesta")).isFalse();
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
}
