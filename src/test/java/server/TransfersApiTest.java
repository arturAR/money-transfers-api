package server;

import com.google.gson.Gson;
import model.dto.ErrorMessage;
import model.entity.Transfer;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransfersApiTest {

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
    void testGetAllTransfers_ReturnAllTransfers() throws Exception {
        ContentResponse res = TEST_ENV.httpClient().GET("http://localhost:4567/transfers");

        assertEquals(
                "[{\"id\":3,\"amount\":500.00,\"fromAcc\":{\"id\":2,\"number\":\"1112\"},\"toAcc\":{\"id\":1,\"number\":\"1111\"},\"timestamp\":\"Dec 11, 2018 1:00:00 PM\"}," +
                        "{\"id\":1,\"amount\":200.00,\"fromAcc\":{\"id\":1,\"number\":\"1111\"},\"toAcc\":{\"id\":2,\"number\":\"1112\"},\"timestamp\":\"Dec 10, 2018 11:00:00 AM\"}," +
                        "{\"id\":2,\"amount\":200.00,\"fromAcc\":{\"id\":1,\"number\":\"1111\"},\"toAcc\":{\"id\":2,\"number\":\"1112\"},\"timestamp\":\"Dec 9, 2018 12:00:00 PM\"}]",
                res.getContentAsString());
    }

    @Test
    void testGetAllTransfers_ReturnTransfersSortedByDateDesc() throws Exception {
        ContentResponse res = TEST_ENV.httpClient().GET("http://localhost:4567/transfers");
        Gson gson = new Gson();
        Transfer[] trs = gson.fromJson(res.getContentAsString(), Transfer[].class);

        assertTrue(trs[0].timestamp.compareTo(trs[1].timestamp) >= 0);
        assertTrue(trs[1].timestamp.compareTo(trs[2].timestamp) >= 0);
    }

    @Test
    void testGetTransferById_ReturnTransferWithSameId() throws Exception {
        ContentResponse res = TEST_ENV.httpClient().GET("http://localhost:4567/transfers/1");

        assertEquals(
                "{\"id\":1,\"amount\":200.00,\"fromAcc\":{\"id\":1,\"number\":\"1111\"},\"toAcc\":{\"id\":2,\"number\":\"1112\"},\"timestamp\":\"Dec 10, 2018 11:00:00 AM\"}",
                res.getContentAsString());
    }

    @Test
    void testGetTransferById_WhenNonNumberId_ReturnErrorMessage() throws Exception {
        ContentResponse res = TEST_ENV.httpClient().GET("http://localhost:4567/transfers/xyz");

        Gson gson = new Gson();
        ErrorMessage e = gson.fromJson(res.getContentAsString(), ErrorMessage.class);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY_422, res.getStatus());
        assertEquals("Validation error", e.msg);
    }

    @Test
    void testGetTransferById_WhenMissedId_ReturnInternalServerError() throws Exception {
        ContentResponse res = TEST_ENV.httpClient().GET("http://localhost:4567/transfers/333");

        Gson gson = new Gson();
        ErrorMessage e = gson.fromJson(res.getContentAsString(), ErrorMessage.class);

        assertEquals(HttpStatus.NOT_FOUND_404, res.getStatus());
        assertEquals("Requested entity not found", e.msg);
    }

    @Test
    void testTransferRequest_ReturnStatusCreated() throws Exception {
        Request req = TEST_ENV.httpClient().POST("http://localhost:4567/transfers");
        req.content(new StringContentProvider("{\"fromAcc\":1,\"toAcc\":2,\"amount\":100}"));
        ContentResponse res = req.send();

        assertEquals(HttpStatus.CREATED_201, res.getStatus());
    }

    @Test
    void testRequestTransfer_WhenTransferFromUnknownAcc_ReturnErrorMessage() throws Exception {
        Request req = TEST_ENV.httpClient().POST("http://localhost:4567/transfers");
        req.content(new StringContentProvider("{\"fromAcc\":100,\"toAcc\":2,\"amount\":100}"));
        ContentResponse res = req.send();

        Gson gson = new Gson();
        ErrorMessage e = gson.fromJson(res.getContentAsString(), ErrorMessage.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, req.send().getStatus());
        assertEquals("Data access error", e.msg);
    }
}
