package com.hashnot.silverexchange;

import org.junit.jupiter.api.Test;

import static com.hashnot.silverexchange.TestModelFactory.market;
import static com.hashnot.silverexchange.util.BigDecimalsTest.ONE;
import static com.hashnot.silverexchange.util.BigDecimalsTest.TWO;
import static org.junit.jupiter.api.Assertions.*;

class OfferRateTest {
    @Test
    void testNullValueIsMarket() {
        OfferRate r = new OfferRate(null);
        assertTrue(r.isMarket());
    }

    @Test
    void testNullValueToStringNoError() {
        OfferRate r = new OfferRate(null);
        assertNotNull(r.toString());
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

    @Test
    void testCompareEqual() {
        OfferRate r1 = new OfferRate(ONE);
        OfferRate r2 = new OfferRate(ONE);
        assertEquals(0, r1.compareTo(r2));
        assertEquals(0, r2.compareTo(r1));
    }

    @Test
    void testCompareDifferent() {
        OfferRate r1 = new OfferRate(ONE);
        OfferRate r2 = new OfferRate(TWO);
        assertTrue(r1.compareTo(r2) < 0);
        assertTrue(r2.compareTo(r1) > 0);
    }

    @Test
    void testCompare2NullsError() {
        OfferRate r1 = market();
        OfferRate r2 = market();
        assertThrows(IllegalArgumentException.class, () -> r1.compareTo(r2));
        assertThrows(IllegalArgumentException.class, () -> r2.compareTo(r1));
    }

    @Test
    void testCompareAgainstNull() {
        OfferRate r1 = new OfferRate(ONE);
        OfferRate r2 = market();
        assertTrue(r1.compareTo(r2) < 0);
        assertTrue(r2.compareTo(r1) > 0);
    }
}
