package com.hashnot.silverexchange;

import java.math.BigDecimal;
import java.util.Objects;

public abstract class AbstractRate {
    /**
     * May be null in case of market order
     */
    protected final BigDecimal value;

    AbstractRate(BigDecimal value) {
        this.value = value;
    }

    @Override
    abstract public boolean equals(Object obj);

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toPlainString();
    }
}
