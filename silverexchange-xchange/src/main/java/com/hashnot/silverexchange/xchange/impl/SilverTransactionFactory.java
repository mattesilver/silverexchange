package com.hashnot.silverexchange.xchange.impl;

import com.hashnot.silverexchange.TransactionRate;
import com.hashnot.silverexchange.ext.ITransactionFactory;
import com.hashnot.silverexchange.match.Transaction;
import com.hashnot.silverexchange.xchange.model.SilverTransaction;
import com.hashnot.silverexchange.xchange.service.IIdGenerator;
import com.hashnot.silverexchange.xchange.util.Clock;

import java.math.BigDecimal;

public class SilverTransactionFactory implements ITransactionFactory {
    final private IIdGenerator idGenerator;
    final private Clock clock;

    public SilverTransactionFactory(IIdGenerator idGenerator, Clock clock) {
        this.idGenerator = idGenerator;
        this.clock = clock;
    }

    @Override
    public Transaction apply(BigDecimal amount, TransactionRate rate) {
        return new SilverTransaction(idGenerator.get(), amount, rate, clock.get());
    }
}
