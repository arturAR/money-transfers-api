package server.routes;

import server.service.AccountsService;
import spark.Route;

public class AccountsRoutes {
    private final AccountsService accountsService;

    public AccountsRoutes(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    public Route getAccounts() {
        return (request, response) -> accountsService.getAllAccounts();
    }
}
