package com.hashnot.silverexchange;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

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
        assertEquals(ONE, r.getValue());
    }

    @Test
    void testHashCode() {
        class MyAbstractRate extends AbstractRate {
            private MyAbstractRate(BigDecimal value) {
                super(value);
            }

            @Override
            public boolean equals(Object obj) {
                return false;
            }
        }

        AbstractRate r = new MyAbstractRate(ONE);
        AbstractRate p = new MyAbstractRate(ONE);

        assertEquals(p.hashCode(), r.hashCode());
    }

    @Test
    void testToString() {
        AbstractRate r = new AbstractRate(ONE) {
            @Override
            public boolean equals(Object obj) {
                return false;
            }
        };

        assertEquals("1", r.toString());
    }


}