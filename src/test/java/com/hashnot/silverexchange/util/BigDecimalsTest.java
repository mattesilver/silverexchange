package com.hashnot.silverexchange.util;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.hashnot.silverexchange.util.BigDecimals.gtz;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BigDecimalsTest {

    public static final BigDecimal MINUS_ONE = BigDecimal.ONE.negate();

    public static final BigDecimal ZERO = BigDecimal.ZERO;
    public static final BigDecimal ONE = BigDecimal.ONE;
    public static final BigDecimal TWO = new BigDecimal(2);
    public static final BigDecimal THREE = new BigDecimal(3);

    @Test
    void testGtzOnPositive() {
        assertTrue(gtz(ONE));
    }

    @Test
    void testGtzOnNegative() {
        BigDecimal neg = ONE.negate();
        assertFalse(gtz(neg));
    }

    @Test
    void testGtzOnZero() {
        assertFalse(gtz(ZERO));
    }
}
