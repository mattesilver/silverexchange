package com.hashnot.silverexchange;

import org.junit.jupiter.api.Test;

import static java.math.BigDecimal.ONE;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AbstractRateTest {
    @Test
    void testCtorFields() {
        AbstractRate r = new AbstractRate(ONE) {
            @Override
            public boolean equals(Object obj) {
                return false;
            }
        };
        assertEquals(ONE, r.value);
    }
}