package com.hashnot.silver.engine;

import java.math.BigDecimal;
import java.util.Objects;

public class Transaction {
    private final BigDecimal amount;
    private final BigDecimal rate;

    public Transaction(BigDecimal amount, BigDecimal rate) {
        this.rate = rate;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return amount + "@" + rate;
    }

    @Override
    public boolean equals(Object o) {
        return
                this == o
                        || (o instanceof Transaction
                        && equals((Transaction) o))
                ;
    }

    private boolean equals(Transaction t) {
        return
                amount.equals(t.amount)
                        && rate.equals(t.rate)
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, rate);
    }
}
