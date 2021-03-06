package server;

import db.MemoryDatabase;
import org.eclipse.jetty.client.HttpClient;
import org.jooq.DSLContext;
import spark.Spark;

public class TestEnv {
    private final TestMemoryDatabaseWrapper db = new TestMemoryDatabaseWrapper();
    private final RestApiServer restServer = new RestApiServer(db);
    private final HttpClient httpClient = new HttpClient();

    public void setUpAll() throws Exception {
        httpClient.start();

        restServer.start();
        Spark.awaitInitialization();
    }

    public void setUp() {
        db.wrap(new MemoryDatabase());
    }

    public void tearDown() {
        db.shutdown();
    }

    public void tearDownAll() throws Exception {
        httpClient.stop();

        Spark.stop();
        //Wait, until Spark server is fully stopped
        Thread.sleep(2000);
    }

    public HttpClient httpClient() {
        return httpClient;
    }

    public static class TestMemoryDatabaseWrapper extends MemoryDatabase {
        private MemoryDatabase wrappedInstance;

        TestMemoryDatabaseWrapper() {}

        public TestMemoryDatabaseWrapper(MemoryDatabase memoryDatabase) {
            wrappedInstance = memoryDatabase;
        }

        void wrap(MemoryDatabase memoryDatabase) {
            this.wrappedInstance = memoryDatabase;
        }

        void shutdown() {
            wrappedInstance.ctx().execute("SHUTDOWN IMMEDIATELY");
        }

        @Override
        public DSLContext ctx() {
            return wrappedInstance.ctx();
        }
    }
}
