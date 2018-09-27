package example.micronaut;

import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

public class OpenApiTest {

    private static EmbeddedServer server;
    private static HttpClient client;

    @BeforeClass
    public static void setupServer() {
        server = ApplicationContext.run(EmbeddedServer.class);
        client = server
                .getApplicationContext()
                .createBean(HttpClient.class, server.getURL());
    }

    @AfterClass
    public static void stopServer() {
        if (server != null) {
            server.stop();
        }
        if (client != null) {
            client.stop();
        }
    }

    @Test
    public void testOpenApiContainsHealthEndpoint()  {
        String yamlString = client.toBlocking().retrieve(HttpRequest.GET("/swagger/hello-world-0.0.yml"), String.class);
        assertNotNull(yamlString);
        Yaml yaml = new Yaml();
        Map<String, Object> m = yaml.load(yamlString);
        assertNotNull(m);
        assertTrue(m.containsKey("paths"));
        assertTrue(m.get("paths") instanceof Map);
        assertTrue(((Map)m.get("paths")).containsKey("/health"));
    }


    @Test
    public void testOpenApiContainsLoginEndpoint()  {
        String yamlString = client.toBlocking().retrieve(HttpRequest.GET("/swagger/hello-world-0.0.yml"), String.class);
        assertNotNull(yamlString);
        Yaml yaml = new Yaml();
        Map<String, Object> m = yaml.load(yamlString);
        assertNotNull(m);
        assertTrue(m.containsKey("paths"));
        assertTrue(m.get("paths") instanceof Map);
        assertTrue(((Map)m.get("paths")).containsKey("/login"));
    }
}