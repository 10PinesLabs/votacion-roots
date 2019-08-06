package ar.com.kfgodel.temas.apiRest;

import ar.com.kfgodel.temas.application.Application;
import ar.com.kfgodel.temas.application.TemasApplication;
import ar.com.kfgodel.temas.config.TemasConfiguration;
import ar.com.kfgodel.temas.helpers.TestConfig;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.IOException;

public abstract class ResourceTest {

    private static final String HOST = "http://localhost:9090";

    private static Thread serverThread;
    private static Application application;

    @BeforeClass
    public static void setUp() {
        serverThread = new Thread(() -> {
            TemasConfiguration applicationConfig = TestConfig.create();
            application = TemasApplication.create(applicationConfig);
            application.start();
        });
        serverThread.start();
        waitForServer();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        application.stop();
        serverThread.join();
    }

    private static void waitForServer() {
        while (!serverIsUp());
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
}
