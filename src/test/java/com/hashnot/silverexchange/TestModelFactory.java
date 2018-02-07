package com.hashnot.silverexchange;

import com.hashnot.silverexchange.match.Offer;
import com.hashnot.silverexchange.match.Side;

import java.math.BigDecimal;

public class TestModelFactory {
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

    static Exchange n() {
        return new Exchange();
    }
}
