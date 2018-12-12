package server;

import com.google.gson.Gson;
import model.dto.ErrorMessage;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountsApiTest {

    private static final TestEnv TEST_ENV = new TestEnv();

    @BeforeAll
    static void setUpAll() throws Exception {
        TEST_ENV.setUpAll();
    }

    @BeforeEach
    void setUp() throws Exception {
        TEST_ENV.setUp();
    }

    @AfterEach
    void tearDown() throws Exception {
        TEST_ENV.tearDown();
    }

    @AfterAll
    static void tearDownAll() throws Exception {
        TEST_ENV.tearDownAll();
    }

    @Test
    void testGetAccounts_ReturnAllAccounts() throws Exception {
        ContentResponse res = TEST_ENV.httpClient().GET("http://localhost:4567/accounts");

        assertEquals(
                "[{\"id\":1,\"number\":\"1111\",\"balance\":500.00},{\"id\":2,\"number\":\"1112\",\"balance\":1000.00}]",
                res.getContentAsString());
    }

    @Test
    void testGetAccountById_ReturnAccountWithSameId() throws Exception {
        ContentResponse res = TEST_ENV.httpClient().GET("http://localhost:4567/accounts/1");

        assertEquals("{\"id\":1,\"number\":\"1111\",\"balance\":500.00}", res.getContentAsString());
    }

    @Test
    void testGetAccountById_WhenMissedId_ReturnInternalServerError() throws Exception {
        ContentResponse res = TEST_ENV.httpClient().GET("http://localhost:4567/accounts/333");

        Gson gson = new Gson();
        ErrorMessage e = gson.fromJson(res.getContentAsString(), ErrorMessage.class);

        assertEquals(HttpStatus.NOT_FOUND_404, res.getStatus());
        assertEquals("Requested entity not found", e.msg);
    }

    @Test
    void testGetAllTransfersForAccountById_ReturnAccountTransfers() throws Exception {
        ContentResponse res = TEST_ENV.httpClient().GET("http://localhost:4567/accounts/2/transfers");

        assertEquals(
                "[{\"id\":3,\"amount\":500.00,\"fromAcc\":{\"id\":2,\"number\":\"1112\"},\"toAcc\":{\"id\":1,\"number\":\"1111\"},\"timestamp\":\"Dec 11, 2018 1:00:00 PM\"}," +
                        "{\"id\":1,\"amount\":200.00,\"fromAcc\":{\"id\":1,\"number\":\"1111\"},\"toAcc\":{\"id\":2,\"number\":\"1112\"},\"timestamp\":\"Dec 10, 2018 11:00:00 AM\"}," +
                        "{\"id\":2,\"amount\":200.00,\"fromAcc\":{\"id\":1,\"number\":\"1111\"},\"toAcc\":{\"id\":2,\"number\":\"1112\"},\"timestamp\":\"Dec 9, 2018 12:00:00 PM\"}]",
                res.getContentAsString());
    }

    @Test
    void testCreateNewAccount_ReturnCreatedAccount() throws Exception {
        Request req = TEST_ENV.httpClient().POST("http://localhost:4567/accounts");
        req.content(new StringContentProvider("{\"number\":\"1113\", \"balance\":700}"));
        ContentResponse res = req.send();

        assertEquals("{\"id\":3,\"number\":\"1113\",\"balance\":700.00}", res.getContentAsString());
    }
}
