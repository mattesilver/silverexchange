package com.hashnot.silverexchange;

import java.math.BigDecimal;
import java.util.Objects;

public class Transaction {
    private final BigDecimal amount;
    private final TransactionRate rate;

    public Transaction(BigDecimal amount, TransactionRate rate) {
        assert amount != null : "Null transaction amount";
        assert rate != null : "Null transaction rate";

        this.amount = amount;
        this.rate = rate;
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
        return Objects.hash(
                amount,
                rate
        );
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionRate getRate() {
        return rate;
    }
}
