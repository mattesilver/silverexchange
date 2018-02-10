package com.hashnot.silverexchange.util;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.hashnot.silverexchange.util.BigDecimals.gtz;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BigDecimalsTest {

    public static final BigDecimal MINUS_ONE = BigDecimal.ONE.negate();
    public static final BigDecimal TWO = new BigDecimal(2);

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
