package ar.com.kfgodel.temas.apiRest;

import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.temas.application.Application;
import ar.com.kfgodel.temas.application.TemasApplication;
import ar.com.kfgodel.temas.config.AuthenticatedTestConfig;
import ar.com.kfgodel.temas.config.TemasConfiguration;
import ar.com.kfgodel.temas.helpers.PersistentTestHelper;
import ar.com.kfgodel.temas.helpers.TestHelper;
import convention.rest.api.ReunionResource;
import convention.rest.api.TemaDeReunionResource;
import convention.services.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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
    private static TemasApplication application;
    TemaService temaService;
    UsuarioService usuarioService;
    ReunionService reunionService;
    MinutaService minutaService;
    TemaGeneralService temaGeneralService;
    TestHelper helper = new TestHelper();
    PersistentTestHelper persistentHelper;
    private HttpClient client;

    @BeforeClass
    public static void applicationSetUp() {
        serverThread = new Thread(() -> {
            TemasConfiguration applicationConfig = new AuthenticatedTestConfig();
            application = (TemasApplication) TemasApplication.create(applicationConfig);
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
        usuarioService.save(helper.unFeche());
        usuarioService.save(helper.unSandro());

        client = HttpClientBuilder.create().build();
        getClient().execute(new HttpGet(pathRelativeToHost("j_security_check")));
        persistentHelper = new PersistentTestHelper(getApplication());
    }

    @After
    public void tearDown() {
        application.clearServices();
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

    HttpResponse makeGetRequest(String aPathRelativeToApi) throws IOException {
        HttpGet request = new HttpGet(pathRelativeToApi(aPathRelativeToApi));
        return getClient().execute(request);
    }

    HttpResponse makeDeleteRequest(String aPathRelativeToApi) throws IOException {
        HttpDelete request = new HttpDelete(pathRelativeToApi(aPathRelativeToApi));
        return getClient().execute(request);
    }

    void assertThatResponseStatusCodeIs(int aResponse, int expectedStatusCode) {
        assertThat(aResponse).isEqualTo(expectedStatusCode);
    }
}
