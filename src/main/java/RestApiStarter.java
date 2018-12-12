import db.MemoryDatabase;
import server.RestApiServer;

class RestApiStarter {

    public static void main(String[] args) {
        MemoryDatabase db = new MemoryDatabase();
        RestApiServer server = new RestApiServer(db);

        server.start();
    }
}
