package server;

import db.InMemoryRepository;
import server.routes.AccountsRoutes;
import server.routes.TransfersRoutes;
import server.service.AccountsService;
import server.service.TransfersService;
import server.transformer.JsonTransformer;

import static spark.Spark.get;
import static spark.Spark.post;

public class RestApiServer {
    private final JsonTransformer transformer = new JsonTransformer();
    private final AccountsRoutes accountsRoutes;
    private final TransfersRoutes transfersRoutes;

    public RestApiServer(InMemoryRepository repository) {
        accountsRoutes = new AccountsRoutes(new AccountsService(repository));
        transfersRoutes = new TransfersRoutes(new TransfersService(repository));
    }

    public void start() {
        buildAccountsApi();
        buildTransfersApi();
    }

    private void buildAccountsApi() {
        get("/accounts", accountsRoutes.getAccounts(), transformer);

        get("/accounts/:id", accountsRoutes.getAccountById(), transformer);

        get("/accounts/:id/transfers", accountsRoutes.getAccountTransfersById(), transformer);

        post("/accounts", accountsRoutes.createAccount(), transformer);
    }

    private void buildTransfersApi() {
        get("/transfers", transfersRoutes.getTransfers(), transformer);

        get("/transfers/:id", transfersRoutes.getTransferById(), transformer);

        post("/transfers", transfersRoutes.performTransfer(), transformer);
    }
}
