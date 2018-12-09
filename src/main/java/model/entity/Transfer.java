package model.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Transfer {
    public long id;
    public BigDecimal amount;
    public Account fromAcc;
    public Account toAcc;
    public Date timestamp;

    public Transfer(long id, BigDecimal amount, Account fromAcc, Account toAcc) {
        this.id = id;
        this.amount = amount;
        this.fromAcc = fromAcc;
        this.toAcc = toAcc;
    }
}
