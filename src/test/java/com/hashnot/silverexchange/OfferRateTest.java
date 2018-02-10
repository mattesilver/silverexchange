package com.hashnot.silverexchange;

import org.junit.jupiter.api.Test;

import static java.math.BigDecimal.ONE;
import static org.junit.jupiter.api.Assertions.*;

// TODO add more tests
class OfferRateTest {
    @Test
    void testNullValueIsMarket() {
        OfferRate r = new OfferRate(null);
        assertTrue(r.isMarket());
    }

    @Test
    void testNullValue() {
        OfferRate r = new OfferRate(null);
        assertNull(r.value);
    }

    @Test
    void testNonNullValue() {
        OfferRate r = new OfferRate(ONE);
        assertEquals(ONE, r.value);
    }

    @Test
    void testNonNullValueNotMarket() {
        OfferRate r = new OfferRate(ONE);
        assertFalse(r.isMarket());
    }

    @Test
    void testCompareEqualLimitOrders() {
        OfferRate r1 = new OfferRate(ONE),
                r2 = new OfferRate(ONE);
        assertEquals(r1, r2);
        assertEquals(r1.compareTo(r2), 0);
    }
}