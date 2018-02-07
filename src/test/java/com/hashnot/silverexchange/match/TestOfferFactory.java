package com.hashnot.silverexchange.match;

import java.math.BigDecimal;

public class TestOfferFactory {
    private static final Object PAIR = new Object() {
        @Override
        public String toString() {
            return "[pair]";
        }
    };

    public static Offer ask(BigDecimal amount, BigDecimal rate) {
        return new Offer(PAIR, Side.Ask, amount, rate);
    }

    public static Offer bid(BigDecimal amount, BigDecimal rate) {
        return new Offer(PAIR, Side.Bid, amount, rate);
    }
}
