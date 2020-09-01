package ar.com.kfgodel.temas.apiRest;

import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.temas.application.Application;
import ar.com.kfgodel.temas.config.AuthenticatedTestConfig;
import ar.com.kfgodel.temas.exceptions.TypeTransformerException;
import ar.com.kfgodel.temas.helpers.PersistentTestHelper;
import ar.com.kfgodel.temas.helpers.ResourceTestApplication;
import ar.com.kfgodel.temas.helpers.TestHelper;
import ar.com.kfgodel.transformbyconvention.api.TypeTransformer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import convention.persistent.TemaDeMinuta;
import convention.persistent.TemaDeReunion;
import convention.persistent.Usuario;
import convention.rest.api.ReunionResource;
import convention.rest.api.TemaDeReunionResource;
import convention.rest.api.tos.TemaDeMinutaTo;
import convention.rest.api.tos.TemaDeReunionTo;
import convention.services.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class ResourceTest {

    private static final String HOST = "http://localhost:9090/";

    private static Thread serverThread;
    private static ResourceTestApplication application;
    private static AuthenticatedTestConfig applicationConfig;
    TemaService temaService;
    UsuarioService usuarioService;
    ReunionService reunionService;
    MinutaService minutaService;
    TemaDeMinutaService temaDeMinutaService;
    TemaGeneralService temaGeneralService;
    TestHelper helper;
    PersistentTestHelper persistentHelper;
    private HttpClient client;

    @BeforeClass
    public static void applicationSetUp() {
        serverThread = new Thread(() -> {
            applicationConfig = new AuthenticatedTestConfig();
            application = new ResourceTestApplication(applicationConfig);
            application.start();
        });
        serverThread.start();
        waitForServer();
    }

    @AfterClass
    public static void applicationTearDown() throws Exception {
        application.stop();
        serverThread.join();
    }

    private static void waitForServer() {
        while (!serverIsUp()) ;
    }

    private static Boolean serverIsUp() {
        HttpClient client = HttpClientBuilder.create().build();
        try {
            client.execute(new HttpGet(HOST));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    static Application getApplication() {
        return application;
    }

    static DependencyInjector getInjector() {
        return getApplication().injector();
    }

    @Before
    public void setUp() throws IOException {
        ReunionResource.create(getInjector());
        TemaDeReunionResource.create(getInjector());
        reunionService = getApplication().getImplementationFor(ReunionService.class);
        temaService = getApplication().getImplementationFor(TemaService.class);
        temaGeneralService = getApplication().getImplementationFor(TemaGeneralService.class);
        usuarioService = getApplication().getImplementationFor(UsuarioService.class);
        minutaService = getApplication().getImplementationFor(MinutaService.class);
        temaDeMinutaService = getApplication().getImplementationFor(TemaDeMinutaService.class);

        helper = getInjector().createInjected(TestHelper.class);
        usuarioService.save(helper.unFeche());
        usuarioService.save(helper.unSandro());

        client = HttpClientBuilder.create().build();
        getClient().execute(new HttpGet(pathRelativeToHost("j_security_check")));
        persistentHelper = new PersistentTestHelper(getApplication());
    }

    @After
    public void tearDown() {
        application.restartOrmModule();
    }

    private String pathRelativeToHost(String aRelativePath) {
        return HOST + aRelativePath;
    }

    private String pathRelativeToApi(String aRelativePath) {
        return pathRelativeToHost("api/v1/" + aRelativePath);
    }

    private HttpClient getClient() {
        return client;
    }

    String getResponseBody(HttpResponse aResponse) throws IOException {
        return new BasicResponseHandler().handleResponse(aResponse);
    }

    int getStatusCode(HttpResponse aResponse) {
        return aResponse.getStatusLine().getStatusCode();
    }

    HttpResponse makeJsonPostRequest(String aRequestPathRelativeToApi, String aJsonString) throws IOException {
        HttpPost request = new HttpPost(pathRelativeToApi(aRequestPathRelativeToApi));
        request.setEntity(new StringEntity(aJsonString, ContentType.APPLICATION_JSON));
        return getClient().execute(request);
    }

    HttpResponse makeJsonPutRequest(String aRequestPathRelativeToApi, String aJsonString) throws IOException {
        HttpPut request = new HttpPut(pathRelativeToApi(aRequestPathRelativeToApi));
        request.setEntity(new StringEntity(aJsonString, ContentType.APPLICATION_JSON));
        return getClient().execute(request);
    }

    HttpResponse makeGetRequest(String aPathRelativeToApi) throws IOException {
        HttpGet request = new HttpGet(pathRelativeToApi(aPathRelativeToApi));
        return getClient().execute(request);
    }

    HttpResponse makeDeleteRequest(String aPathRelativeToApi) throws IOException {
        HttpDelete request = new HttpDelete(pathRelativeToApi(aPathRelativeToApi));
        return getClient().execute(request);
    }

    HttpResponse makePatchRequest(String aPathRelativeToApi, String aJsonString) throws IOException {
        HttpPatch request = new HttpPatch(pathRelativeToApi(aPathRelativeToApi));
        request.setEntity(new StringEntity(aJsonString, ContentType.APPLICATION_JSON));
        return getClient().execute(request);
    }

    void assertThatResponseStatusCodeIs(HttpResponse aResponse, int expectedStatusCode) {
        assertThat(getStatusCode(aResponse)).isEqualTo(expectedStatusCode);
    }

    TemaDeReunionTo convertirATo(TemaDeReunion unTemaDeReunion) {
        return convertirA(unTemaDeReunion, TemaDeReunionTo.class);
    }

    <T> T convertirA(Object unObjeto, Class<T> unaClaseDestino) {
        return getTypeTransformer().transformTo(unaClaseDestino, unObjeto);
    }

    TemaDeMinutaTo convertirATo(TemaDeMinuta unTemaDeMinuta) {
        return getTypeTransformer().transformTo(TemaDeMinutaTo.class, unTemaDeMinuta);
    }

    String convertirAJsonString(Object unObjeto) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(unObjeto);
    }

    private TypeTransformer getTypeTransformer() {
        return getInjector().getImplementationFor(TypeTransformer.class)
            .orElseThrow(() -> new TypeTransformerException("no se ha injectado ningún TypeTransformer"));
    }

    Usuario getAuthenticatedUser() {
        return usuarioService.get(applicationConfig.getAuthenticatedUserId());
    }
}
