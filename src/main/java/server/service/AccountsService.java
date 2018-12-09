package server.service;

import db.InMemoryRepository;
import model.dto.AccountRequest;
import model.entity.Account;
import model.entity.Transfer;

import java.util.List;

public class AccountsService {
    private final InMemoryRepository repository;

    public AccountsService(InMemoryRepository repository) {
        this.repository = repository;
    }

    public List<Account> getAllAccounts() {
        return repository.getAllAccounts();
    }

    public Account getAccount(long accId) {
        return repository.getAccountById(accId);
    }

    public List<Transfer> getAccountTransfers(long accId) {
        return repository.getAccountTransfers(accId);
    }

    public Account createAccount(AccountRequest acc) {
        return repository.createAccount(acc);
    }
}
