package com.hashnot.silverexchange;

import com.hashnot.silverexchange.ext.ITransactionFactory;
import com.hashnot.silverexchange.match.Offer;
import com.hashnot.silverexchange.match.Side;
import com.hashnot.silverexchange.match.Transaction;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class TestModelFactory {
    private static final Object PAIR = new Object() {
        @Override
        public String toString() {
            return "[pair]";
        }
    };

    public static final ITransactionFactory TX_FACTORY = Transaction::new;

    public static Offer ask(BigDecimal amount, BigDecimal rate) {
        return new Offer(PAIR, Side.Ask, amount, new OfferRate(rate));
    }

    public static Offer ask(BigDecimal amount, OfferRate rate) {
        return new Offer(PAIR, Side.Ask, amount, rate);
    }

    public static Offer bid(BigDecimal amount, BigDecimal rate) {
        return new Offer(PAIR, Side.Bid, amount, new OfferRate(rate));
    }

    public static Offer bid(BigDecimal amount, OfferRate rate) {
        return new Offer(PAIR, Side.Bid, amount, rate);
    }

    static Exchange n() {
        return new Exchange(TX_FACTORY);
    }

    public static Transaction tx(BigDecimal amount, BigDecimal rate) {
        return new Transaction(amount, new TransactionRate(rate));
    }

    static OrderBook b() {
        return new OrderBook(TX_FACTORY);
    }

    public static Map<Side, List<Offer>> sides(List<Offer> bids, List<Offer> asks) {
        Map<Side, List<Offer>> result = new EnumMap<>(Side.class);
        result.put(Side.Bid, bids);
        result.put(Side.Ask, asks);
        return result;
    }
}
