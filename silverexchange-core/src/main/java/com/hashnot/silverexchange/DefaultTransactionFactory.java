package com.hashnot.silverexchange;

import com.hashnot.silverexchange.match.Transaction;
import com.hashnot.silverexchange.util.Clock;

import java.math.BigDecimal;

public class DefaultTransactionFactory implements ITransactionFactory {
    final private Clock clock;

    public DefaultTransactionFactory(Clock clock) {
        this.clock = clock;
    }

    @Override
    public Transaction apply(BigDecimal amount, TransactionRate rate) {
        return new Transaction(amount, rate, clock.get());
    }
}
