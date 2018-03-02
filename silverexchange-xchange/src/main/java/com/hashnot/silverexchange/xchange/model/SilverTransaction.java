package com.hashnot.silverexchange.xchange.model;

import com.hashnot.silverexchange.Transaction;
import com.hashnot.silverexchange.TransactionRate;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class SilverTransaction extends Transaction {
    private UUID id;
    private Instant timestamp;

    public SilverTransaction(UUID id, BigDecimal amount, TransactionRate rate, Instant timestamp) {
        super(amount, rate);
        this.id = id;
        this.timestamp = timestamp;
    }

    public UUID getId() {
        return id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
