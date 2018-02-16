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
        Transaction tx = factory.apply(ONE, rate);

        assertTrue(tx instanceof SilverTransaction);
        SilverTransaction stx = (SilverTransaction) tx;

        assertEquals(ONE, tx.getAmount());
        assertEquals(ID, stx.getId());
        assertEquals(rate, tx.getRate());
        assertEquals(TS, stx.getTimestamp());
    }
}