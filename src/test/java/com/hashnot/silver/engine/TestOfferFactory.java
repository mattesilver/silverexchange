package com.hashnot.silver.engine;

import java.math.BigDecimal;

import static com.hashnot.silver.engine.Side.Ask;
import static com.hashnot.silver.engine.Side.Bid;

//TODO update tests to use this class
public class TestOfferFactory {
    private static final Object PAIR = new Object() {
        @Override
        public String toString() {
            return "[pair]";
        }
    };

    public static Offer ask(BigDecimal amount, BigDecimal rate) {
        return new Offer(PAIR, Ask, amount, rate);
    }

    public static Offer bid(BigDecimal amount, BigDecimal rate) {
        return new Offer(PAIR, Bid, amount, rate);
    }
}
