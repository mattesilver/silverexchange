package com.hashnot.silverexchange.match;

import com.hashnot.silverexchange.Transaction;
import com.hashnot.silverexchange.TransactionRate;

import java.math.BigDecimal;

public class TestTransactionListener implements ITransactionListener<Offer> {
    public Transaction transaction;

    @Override
    public void notifyTransaction(BigDecimal amount, BigDecimal rate, Offer active) {
        transaction = new Transaction(amount, new TransactionRate(rate));
    }
}
