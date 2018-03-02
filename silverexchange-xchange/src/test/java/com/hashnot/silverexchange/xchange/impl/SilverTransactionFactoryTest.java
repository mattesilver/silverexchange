package com.hashnot.silverexchange.xchange.impl;

import com.hashnot.silverexchange.TransactionRate;
import com.hashnot.silverexchange.xchange.model.SilverTransaction;
import org.junit.jupiter.api.Test;

import static com.hashnot.silverexchange.xchange.model.TestModelFactory.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SilverTransactionFactoryTest {
    @Test
    void testCreate() {
        SilverTransactionFactory factory = new SilverTransactionFactory(ID_GEN, CLOCK);

        SilverTransaction tx = factory.create(ONE, TWO, null);

        assertEquals(ONE, tx.getAmount());
        assertEquals(ID, tx.getId());
        assertEquals(new TransactionRate(TWO), tx.getRate());
        assertEquals(TS, tx.getTimestamp());
    }
}
