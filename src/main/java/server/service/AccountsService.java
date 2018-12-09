package server.service;

import db.InMemoryRepository;
import model.entity.Account;

import java.util.List;

public class AccountsService {
    private final InMemoryRepository repository;

    public AccountsService(InMemoryRepository repository) {
        this.repository = repository;
    }

    public List<Account> getAllAccounts() {
        return repository.getAllAccounts();
    }

}
