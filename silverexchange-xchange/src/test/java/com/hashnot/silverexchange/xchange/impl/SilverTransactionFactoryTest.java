package com.hashnot.silverexchange.xchange.impl;

import com.hashnot.silverexchange.TransactionRate;
import com.hashnot.silverexchange.match.Transaction;
import com.hashnot.silverexchange.xchange.model.SilverTransaction;
import org.junit.jupiter.api.Test;

import static com.hashnot.silverexchange.util.BigDecimalsTest.ONE;
import static com.hashnot.silverexchange.util.BigDecimalsTest.TWO;
import static com.hashnot.silverexchange.xchange.model.TestModelFactory.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SilverTransactionFactoryTest {
    @Test
    void testCreate() {
        SilverTransactionFactory factory = new SilverTransactionFactory(ID_GEN, CLOCK);

        TransactionRate rate = new TransactionRate(TWO);
        Transaction transaction = factory.apply(ONE, rate);

        assertTrue(transaction instanceof SilverTransaction);
        assertEquals(ONE, transaction.getAmount());
        assertEquals(rate, transaction.getRate());
        assertEquals(TS, transaction.getTimestamp());

        SilverTransaction tx = (SilverTransaction) transaction;
        assertEquals(ID, tx.getId());
    }
}