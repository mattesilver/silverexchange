package com.hashnot.silverexchange;

import org.junit.jupiter.api.Test;

import static com.hashnot.silverexchange.util.BigDecimalsTest.MINUS_ONE;
import static com.hashnot.silverexchange.util.BigDecimalsTest.TWO;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static org.junit.jupiter.api.Assertions.*;

class TransactionRateTest {

    @Test
    void testNullRateFails() {
        assertThrows(IllegalArgumentException.class, () -> new TransactionRate(null));
    }

    @Test
    void testZeroFails() {
        assertThrows(IllegalArgumentException.class, () -> new TransactionRate(ZERO));
    }

    @Test
    void testNegativeValueFails() {
        assertThrows(IllegalArgumentException.class, () -> new TransactionRate(MINUS_ONE));
    }

    @Test
    void testEquals() {
        TransactionRate t1 = new TransactionRate(ONE),
                t2 = new TransactionRate(ONE);

        assertTrue(t1.equals(t2));
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    void testNotEquals() {
        TransactionRate t1 = new TransactionRate(ONE),
                t2 = new TransactionRate(TWO);
        assertFalse(t1.equals(t2));
    }
}