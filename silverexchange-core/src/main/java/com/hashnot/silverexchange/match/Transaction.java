package com.hashnot.silverexchange.match;

import com.hashnot.silverexchange.OfferRate;
import com.hashnot.silverexchange.TransactionRate;

import java.math.BigDecimal;
import java.util.Objects;

public class Transaction {
    private final BigDecimal amount;
    private final TransactionRate rate;

    public Transaction(BigDecimal amount, OfferRate rate) {
        this(amount, TransactionRate.from(rate));
    }

    public Transaction(BigDecimal amount, TransactionRate rate) {
        assert amount != null : "Null transaction amount";
        assert rate != null : "Null transaction rate";

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
