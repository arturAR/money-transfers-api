package model.entity;

import java.math.BigDecimal;

public class Account {
    public long id;
    public String number;
    public BigDecimal balance;

    public Account(long id, String number, BigDecimal balance) {
        this.id = id;
        this.number = number;
        this.balance = balance;
    }
}
