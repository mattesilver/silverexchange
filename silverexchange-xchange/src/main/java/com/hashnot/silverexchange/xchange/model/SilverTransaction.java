package com.hashnot.silverexchange.xchange.model;

import com.hashnot.silverexchange.TransactionRate;
import com.hashnot.silverexchange.match.Transaction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class SilverTransaction extends Transaction {
    private UUID id;

    public SilverTransaction(UUID id, BigDecimal amount, TransactionRate rate, Instant timestamp) {
        super(amount, rate, timestamp);
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
