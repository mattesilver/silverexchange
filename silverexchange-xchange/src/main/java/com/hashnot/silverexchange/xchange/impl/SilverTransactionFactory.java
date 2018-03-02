package com.hashnot.silverexchange.xchange.impl;

import com.hashnot.silverexchange.TransactionRate;
import com.hashnot.silverexchange.ext.ITransactionFactory;
import com.hashnot.silverexchange.xchange.model.SilverOrder;
import com.hashnot.silverexchange.xchange.model.SilverTransaction;
import com.hashnot.silverexchange.xchange.service.IIdGenerator;
import com.hashnot.silverexchange.xchange.util.Clock;

import java.math.BigDecimal;

public class SilverTransactionFactory implements ITransactionFactory<SilverOrder, SilverTransaction> {
    final private IIdGenerator idGenerator;
    final private Clock clock;

    public SilverTransactionFactory(IIdGenerator idGenerator, Clock clock) {
        this.idGenerator = idGenerator;
        this.clock = clock;
    }

    @Override
    public SilverTransaction create(BigDecimal amount, BigDecimal rate, SilverOrder offer) {
        return new SilverTransaction(idGenerator.get(), amount, new TransactionRate(rate), clock.get());
    }
}
