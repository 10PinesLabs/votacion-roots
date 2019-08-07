package ar.com.kfgodel.temas.apiRest;

import ar.com.kfgodel.dependencies.api.DependencyInjector;
import ar.com.kfgodel.temas.application.Application;
import ar.com.kfgodel.temas.application.TemasApplication;
import ar.com.kfgodel.temas.config.AuthenticatedTestConfig;
import ar.com.kfgodel.temas.config.TemasConfiguration;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class ResourceTest {

    private static final String HOST = "http://localhost:9090/";

    private static Thread serverThread;
    private static Application application;

    @BeforeClass
    public static void applicationSetUp() {
        serverThread = new Thread(() -> {
            TemasConfiguration applicationConfig = new AuthenticatedTestConfig();
            application = TemasApplication.create(applicationConfig);
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

    private static Application getApplication() {
        return application;
    }

    static DependencyInjector getInjector() {
        return getApplication().injector();
    }


    private HttpClient client;

    @Before
    public void setUp() throws IOException {
        client = HttpClientBuilder.create().build();
        getClient().execute(new HttpGet(pathRelativeToHost("j_security_check")));
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

    void assertThatResponseStatusCodeIs(int aResponse, int expectedStatusCode) {
        assertThat(aResponse).isEqualTo(expectedStatusCode);
    }
}
