package server;

import db.InMemoryRepository;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import spark.Spark;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransfersApiTest {
    private final InMemoryRepository repository = new InMemoryRepository();
    private final RestApiServer restServer = new RestApiServer(repository);
    private final HttpClient httpClient = new HttpClient();

    @BeforeAll
    void setUpAll() throws Exception {
        httpClient.start();

        restServer.start();
        Spark.awaitInitialization();
    }

    @AfterAll
    void tearDownAll() throws Exception {
        httpClient.stop();

        Spark.stop();
        //Wait, until Spark server is fully stopped
        Thread.sleep(2000);
    }

    @Test
    void testGetAllTransfers_ReturnAllTransfers() throws Exception {
        ContentResponse res = httpClient.GET("http://localhost:4567/transfers");

        assertEquals(
                "[{\"id\":1,\"amount\":200,\"fromAcc\":{\"id\":1,\"number\":\"1111\",\"balance\":500},\"toAcc\":{\"id\":2,\"number\":\"1112\",\"balance\":1000}},{\"id\":2,\"amount\":200,\"fromAcc\":{\"id\":1,\"number\":\"1111\",\"balance\":500},\"toAcc\":{\"id\":2,\"number\":\"1112\",\"balance\":1000}},{\"id\":3,\"amount\":500,\"fromAcc\":{\"id\":2,\"number\":\"1112\",\"balance\":1000},\"toAcc\":{\"id\":1,\"number\":\"1111\",\"balance\":500}}]",
                res.getContentAsString());
    }

    @Test
    void testGetTransferById_ReturnTransferWithSameId() throws Exception {
        ContentResponse res = httpClient.GET("http://localhost:4567/transfers/1");

        assertEquals(
                "{\"id\":1,\"amount\":200,\"fromAcc\":{\"id\":1,\"number\":\"1111\",\"balance\":500},\"toAcc\":{\"id\":2,\"number\":\"1112\",\"balance\":1000}}",
                res.getContentAsString());
    }

    @Test
    void testGetTransferById_WhenMissedId_ReturnInternalServerError() throws Exception {
        ContentResponse res = httpClient.GET("http://localhost:4567/transfers/333");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, res.getStatus());
    }

    @Test
    void testTransferRequest_ReturnTranfer() throws Exception {
        Request req = httpClient.POST("http://localhost:4567/transfers");
        req.content(new StringContentProvider("{\"fromAcc\":1,\"toAcc\":2,\"amount\":100}"));
        ContentResponse res = req.send();

        assertEquals(HttpStatus.CREATED_201, res.getStatus());
        assertEquals(
                "{\"id\":4,\"amount\":100,\"fromAcc\":{\"id\":1,\"number\":\"1111\",\"balance\":500},\"toAcc\":{\"id\":2,\"number\":\"1112\",\"balance\":1000}}",
                res.getContentAsString());
    }
}
