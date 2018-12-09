package server.routes;

import com.google.gson.Gson;
import model.dto.AccountRequest;
import model.entity.Account;
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

    public Route getAccountById() {
        return (request, response) -> {
            String id = request.params(":id");
            long accId = Long.parseLong(id);

            return accountsService.getAccount(accId);
        };
    }

    public Route getAccountTransfersById() {
        return (request, response) -> {
            String id = request.params(":id");
            long accId = Long.parseLong(id);

            return accountsService.getAccountTransfers(accId);
        };
    }

    public Route createAccount() {
        return (request, response) -> {
            Gson gson = new Gson();
            AccountRequest acc = gson.fromJson(request.body(), AccountRequest.class);
            Account account = accountsService.createAccount(acc);

            response.status(201);

            return account;
        };
    }
}
