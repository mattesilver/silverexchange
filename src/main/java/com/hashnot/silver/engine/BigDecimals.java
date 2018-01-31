package com.hashnot.silver.engine;

import java.math.BigDecimal;
import java.util.Comparator;

public class BigDecimals {
    final public static Comparator<BigDecimal> COMPARATOR = new BigDecimalComparator();

    public static BigDecimal max(BigDecimal bd1, BigDecimal bd2) {
        return COMPARATOR.compare(bd1, bd2) >= 0 ? bd1 : bd2;
    }

    public static BigDecimal min(BigDecimal bd1, BigDecimal bd2) {
        return COMPARATOR.compare(bd1, bd2) < 0 ? bd1 : bd2;
    }

    static class BigDecimalComparator implements Comparator<BigDecimal> {
        @Override
        public int compare(BigDecimal o1, BigDecimal o2) {
            assert o1 != null;
            assert o2 != null;
            return o1.compareTo(o2);
        }
    }
}
