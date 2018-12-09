import db.InMemoryRepository;
import server.RestApiServer;

public class RestApiStarter {

    public static void main(String[] args) {
        InMemoryRepository repository = new InMemoryRepository();
        RestApiServer server = new RestApiServer(repository);

        server.start();
    }
}
