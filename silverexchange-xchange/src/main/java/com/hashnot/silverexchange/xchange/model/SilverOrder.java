package com.hashnot.silverexchange.xchange.model;

import com.hashnot.silverexchange.OfferRate;
import com.hashnot.silverexchange.match.Offer;
import com.hashnot.silverexchange.match.Side;
import org.knowm.xchange.currency.CurrencyPair;

import java.math.BigDecimal;
import java.util.UUID;

public class SilverOrder extends Offer {
    final private UUID id;

    public SilverOrder(CurrencyPair pair, Side side, BigDecimal amount, OfferRate rate, UUID id) {
        super(pair, side, amount, rate);
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
