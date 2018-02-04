package com.hashnot.silver.exchange.util;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.hashnot.silver.exchange.util.BigDecimals.gtz;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BigDecimalsTest {

    @Test
    void testGtzOnPositive() {
        assertTrue(gtz(ONE));
    }

    @Test
    void testGtzOnNegative() {
        BigDecimal NEG = ONE.negate();
        assertFalse(gtz(NEG));
    }

    @Test
    void testGtzOnZero() {
        assertFalse(gtz(ZERO));
    }
}
