package com.hashnot.silverexchange.match;

import com.hashnot.silverexchange.OfferRate;
import com.hashnot.silverexchange.TransactionRate;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public class Transaction {
    private final BigDecimal amount;
    private final TransactionRate rate;
    private final Instant timestamp;

    public Transaction(BigDecimal amount, OfferRate rate, Instant timestamp) {
        this(amount, TransactionRate.from(rate), timestamp);
    }

    public Transaction(BigDecimal amount, TransactionRate rate, Instant timestamp) {
        assert amount != null : "Null transaction amount";
        assert rate != null : "Null transaction rate";
        assert timestamp != null : "Null timestamp";

        this.rate = rate;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return amount + "@" + rate + " t:" + timestamp;
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
                        && timestamp.equals(t.timestamp)
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, rate, timestamp);
    }
}
