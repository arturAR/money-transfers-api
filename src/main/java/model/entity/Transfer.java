package model.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Transfer {
    public long id;
    public BigDecimal amount;
    public Account fromAcc;
    public Account toAcc;
    public Date timestamp;
}
