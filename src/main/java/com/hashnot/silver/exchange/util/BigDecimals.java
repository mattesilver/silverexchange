package com.hashnot.silver.exchange.util;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;

public class BigDecimals {
    private BigDecimals() {
        // util class
    }

    /**
     * @return true if num is greater than zero
     */
    public static boolean gtz(BigDecimal num) {
        return num.compareTo(ZERO) > 0;
    }
}
