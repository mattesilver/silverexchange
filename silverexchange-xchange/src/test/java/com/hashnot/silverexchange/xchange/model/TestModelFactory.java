package com.hashnot.silverexchange.xchange.model;

import com.hashnot.silverexchange.OfferRate;
import com.hashnot.silverexchange.TransactionRate;
import com.hashnot.silverexchange.match.Offer;
import com.hashnot.silverexchange.match.Side;
import com.hashnot.silverexchange.xchange.service.IIdGenerator;
import com.hashnot.silverexchange.xchange.util.Clock;
import org.knowm.xchange.currency.CurrencyPair;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

public class TestModelFactory {
    public static final Instant TS = Instant.ofEpochMilli(0);
    public static final Date TS_DATE = Date.from(TS);
    public static final Clock CLOCK = () -> TS;

    public static final UUID ID = new UUID(0L, 0L);
    public static final IIdGenerator ID_GEN = () -> ID;
    public static final String ID_STR = ID.toString();

    public static final CurrencyPair PAIR = CurrencyPair.BTC_EUR;

    public static Map<Side, List<Offer>> sides(List<SilverOrder> bids, List<SilverOrder> asks) {
        Map<Side, List<SilverOrder>> result = new EnumMap<>(Side.class);
        result.put(Side.Bid, bids);
        result.put(Side.Ask, asks);

        return (Map<Side, List<Offer>>) (Map) result;
    }

    public static SilverOrder bid(BigDecimal amount, BigDecimal rate) {
        return new SilverOrder(ID, PAIR, Side.Bid, amount, new OfferRate(rate), TS);
    }

    public static SilverTransaction tx(BigDecimal amount, BigDecimal rate) {
        return new SilverTransaction(ID, amount, new TransactionRate(rate), TS);
    }
}
