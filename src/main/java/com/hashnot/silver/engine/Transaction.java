package com.hashnot.silver.engine;

import java.math.BigDecimal;

public class Transaction {
    final public BigDecimal amount;
    final public BigDecimal rate;

    public Transaction(BigDecimal amount, BigDecimal rate) {
        this.rate = rate;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return amount + "@" + rate;
    }
}
