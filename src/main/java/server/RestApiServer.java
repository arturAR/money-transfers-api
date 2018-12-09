package server;

import db.InMemoryRepository;
import server.routes.AccountsRoutes;
import server.service.AccountsService;
import server.transformer.JsonTransformer;

import static spark.Spark.get;

public class RestApiServer {
    private final JsonTransformer transformer = new JsonTransformer();
    private final AccountsRoutes accRoutes;

    public RestApiServer(InMemoryRepository repository) {
        accRoutes = new AccountsRoutes(new AccountsService(repository));
    }

    public void start() {
        buildAccountsApi();
    }

    private void buildAccountsApi() {
        get("/accounts", accRoutes.getAccounts(), transformer);
    }
}
