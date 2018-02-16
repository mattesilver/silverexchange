package com.hashnot.silverexchange;

import com.hashnot.silverexchange.ext.Clock;
import com.hashnot.silverexchange.ext.ITransactionFactory;
import com.hashnot.silverexchange.match.Transaction;

import java.math.BigDecimal;

public class TestTransactionFactory implements ITransactionFactory {
    final private Clock clock;

    public TestTransactionFactory(Clock clock) {
        this.clock = clock;
    }

    @Override
    public Transaction apply(BigDecimal amount, TransactionRate rate) {
        return new Transaction(amount, rate, clock.get());
    }
}
