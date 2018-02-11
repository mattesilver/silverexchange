package com.hashnot.silverexchange;

import com.hashnot.silverexchange.util.BigDecimals;

import java.math.BigDecimal;
import java.util.Objects;

public class TransactionRate extends AbstractRate {
    TransactionRate(BigDecimal value) {
        super(value);
        if (value == null)
            throw new IllegalArgumentException("Null tx rate value");
        if (!BigDecimals.gtz(value))
            throw new IllegalArgumentException("Non-positive tx rate value");
    }

    public static TransactionRate from(OfferRate offerRate) {
        assert offerRate != null : "Null OfferRate";
        assert offerRate.value != null : "Null OfferRate value";
        return new TransactionRate(offerRate.value);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof TransactionRate && equals((TransactionRate) obj);
    }

    private boolean equals(TransactionRate r) {
        return Objects.equals(value, r.value);
    }

}
