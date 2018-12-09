package db;

import model.entity.Account;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class InMemoryRepository {

    private List<Account> accounts = Arrays.asList(
            new Account(1L, "1111", new BigDecimal(500)),
            new Account(2L, "1112", new BigDecimal(1000)));

    public List<Account> getAllAccounts() {
        return accounts;
    }
}
