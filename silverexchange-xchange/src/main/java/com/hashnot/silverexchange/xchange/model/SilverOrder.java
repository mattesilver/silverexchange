package com.hashnot.silverexchange.xchange.model;

import com.hashnot.silverexchange.OfferRate;
import com.hashnot.silverexchange.match.Offer;
import com.hashnot.silverexchange.match.Side;
import org.knowm.xchange.currency.CurrencyPair;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class SilverOrder extends Offer {
    final private UUID id;
    final private CurrencyPair pair;
    final private Instant timestamp;

    public SilverOrder(UUID id, CurrencyPair pair, Side side, BigDecimal amount, OfferRate rate, Instant timestamp) {
        super(side, amount, rate);
        this.id = id;
        this.pair = pair;
        this.timestamp = timestamp;
    }

    public UUID getId() {
        return id;
    }

    public CurrencyPair getPair() {
        return pair;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
