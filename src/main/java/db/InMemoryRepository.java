package db;

import model.dto.AccountRequest;
import model.dto.TransferRequest;
import model.entity.Account;
import model.entity.Transfer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryRepository {

    private List<Account> accounts = new ArrayList<>(Arrays.asList(
            new Account(1L, "1111", new BigDecimal(500)),
            new Account(2L, "1112", new BigDecimal(1000))));

    private List<Transfer> transfers = new ArrayList<>(Arrays.asList(
            new Transfer(1L, new BigDecimal(200), getAccountById(1l), getAccountById(2l)),
            new Transfer(2L, new BigDecimal(200), getAccountById(1l), getAccountById(2l)),
            new Transfer(3L, new BigDecimal(500), getAccountById(2l), getAccountById(1l))));

    public List<Account> getAllAccounts() {
        return accounts;
    }

    public List<Transfer> getAllTransfers() {
        return transfers;
    }

    public Account getAccountById(long accId) {
        return accounts.stream()
                .filter(account -> account.id == accId)
                .findFirst()
                .get();
    }

    public List<Transfer> getAccountTransfers(long accId) {
        Account from = getAccountById(accId);
        return transfers.stream()
                .filter(transfer -> transfer.fromAcc.id == from.id)
                .collect(Collectors.toList());
    }

    public Account createAccount(AccountRequest acc) {
        long id = accounts.size() + 1;
        accounts.add(new Account(id, acc.number, acc.balance));
        return getAccountById(id);
    }

    public Transfer getTransferById(long trId) {
        return transfers.stream()
                .filter(transfer -> transfer.id == trId)
                .findFirst()
                .get();
    }

    public Transfer performTransfer(TransferRequest trReq) {
        long id = transfers.size() + 1;
        transfers.add(new Transfer(id, trReq.amount, getAccountById(trReq.fromAcc), getAccountById(trReq.toAcc)));
        return getTransferById(id);
    }
}
