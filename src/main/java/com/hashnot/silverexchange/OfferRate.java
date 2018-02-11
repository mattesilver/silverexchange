package com.hashnot.silverexchange;

import com.hashnot.silverexchange.util.BigDecimals;

import java.math.BigDecimal;
import java.util.Objects;

public class OfferRate extends AbstractRate implements Comparable<OfferRate> {

    public OfferRate(BigDecimal value) {
        super(value);
        if (value != null && !BigDecimals.gtz(value))
            throw new IllegalArgumentException("Non-positive rate");

    }

    public boolean isMarket() {
        return value == null;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof OfferRate && equals((OfferRate) obj);
    }

    private boolean equals(OfferRate r) {
        return Objects.equals(value, r.value);
    }

    @Override
    public int compareTo(OfferRate r) {
        assert r != null;
        if (value == null && r.value == null) {
            throw new IllegalArgumentException("The result of comparing two Market Rates is undefined");
        }

        if (value == null)
            return 1;
        else if (r.value == null)
            return -1;
        else
            return value.compareTo(r.value);
    }

    @Override
    public String toString() {
        return value == null ? "market" : super.toString();
    }

}
