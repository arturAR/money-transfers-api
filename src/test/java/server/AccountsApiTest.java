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
class AccountsApiTest {
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
    void testGetAccounts_ReturnAllAccounts() throws Exception {
        ContentResponse res = httpClient.GET("http://localhost:4567/accounts");

        assertEquals(
                "[{\"id\":1,\"number\":\"1111\",\"balance\":500}," +
                        "{\"id\":2,\"number\":\"1112\",\"balance\":1000}]",
                res.getContentAsString());
    }

    @Test
    void testGetAccountById_ReturnAccountWithSameId() throws Exception {
        ContentResponse res = httpClient.GET("http://localhost:4567/accounts/1");

        assertEquals("{\"id\":1,\"number\":\"1111\",\"balance\":500}", res.getContentAsString());
    }

    @Test
    void testGetAccountById_WhenMissedId_ReturnInternalServerError() throws Exception {
        ContentResponse res = httpClient.GET("http://localhost:4567/accounts/333");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, res.getStatus());
    }

    @Test
    void testGetAllTransfersForAccountById_ReturnAccountTransfers() throws Exception {
        ContentResponse res = httpClient.GET("http://localhost:4567/accounts/2/transfers");

        assertEquals(
                "[{\"id\":3,\"amount\":500,\"fromAcc\":{\"id\":2,\"number\":\"1112\",\"balance\":1000},\"toAcc\":{\"id\":1,\"number\":\"1111\",\"balance\":500}}]",
                res.getContentAsString());
    }

    @Test
    void testCreateNewAccount_ReturnCreatedAccount() throws Exception {
        Request req = httpClient.POST("http://localhost:4567/accounts");
        req.content(new StringContentProvider("{\"number\":\"1113\", \"balance\":700}"));
        ContentResponse res = req.send();

        assertEquals("{\"id\":3,\"number\":\"1113\",\"balance\":700}", res.getContentAsString());
    }
}
